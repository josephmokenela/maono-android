package com.com.boha.monitor.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.PopupListAdapter;
import com.com.boha.monitor.library.adapters.StatusReportAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebCheck;
import com.com.boha.monitor.library.util.WebCheckResult;
import com.com.boha.monitor.library.util.WebSocketUtil;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SiteStatusReportFragment extends Fragment implements PageFragment {


    public static SiteStatusReportFragment newInstance(ResponseDTO r) {
        SiteStatusReportFragment fragment = new SiteStatusReportFragment();
        Bundle args = new Bundle();
        args.putSerializable("response", r);
        fragment.setArguments(args);
        return fragment;
    }


    public SiteStatusReportFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context ctx;
    TextView txtCount, txtSiteName;
    ImageView heroImage;
    ProjectSiteDTO projectSite;
    ProjectDTO project;
    TextView txtProject, txtEmpty, txtTitle;
    ListView listView;
    LayoutInflater inflater;
    StatusReportAdapter adapter;
    Button btnStart, btnEnd;
    Date startDate, endDate;
    View view, handle;
    static final Locale locale = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", locale);
    List<ProjectDTO> projectList;
    ListPopupWindow popupWindow;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, "########## onCreateView");
        this.inflater = inflater;
        ctx = getActivity();
        view = inflater.inflate(R.layout.fragment_status_list, container, false);
        setFields();


        return view;
    }

    private void showPopup() {
        List<String> list = new ArrayList<>();
        for (ProjectDTO p : projectList) {
            list.add(p.getProjectName());
        }
        View v = Util.getHeroView(ctx, ctx.getString(R.string.select_project));
        popupWindow = new ListPopupWindow(ctx);
        popupWindow.setPromptView(v);
        popupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_ABOVE);
        popupWindow.setAnchorView(handle);
        popupWindow.setHorizontalOffset(72);
        popupWindow.setWidth(600);
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAdapter(new PopupListAdapter(ctx, R.layout.xxsimple_spinner_item, list, false));
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                project = projectList.get(position);
                txtProject.setText(project.getProjectName());
                getSiteData();
                popupWindow.dismiss();
            }
        });
        popupWindow.show();
    }

    private void setFields() {
        handle = view.findViewById(R.id.STATLST_handle);
        progressBar = (ProgressBar) view.findViewById(R.id.STATLST_progress);
        listView = (ListView) view.findViewById(R.id.STATLST_list);
        heroImage = (ImageView) view.findViewById(R.id.STATLST_heroImage);
        txtProject = (TextView) view.findViewById(R.id.STATLST_project);
        txtCount = (TextView) view.findViewById(R.id.STATLST_txtCount);
        txtTitle = (TextView) view.findViewById(R.id.STATLST_txtTitle);
        txtEmpty = (TextView) view.findViewById(R.id.STATLST_txtEmpty);
        btnEnd = (Button) view.findViewById(R.id.STATLST_endDate);
        btnStart = (Button) view.findViewById(R.id.STATLST_startDate);
        txtSiteName = (TextView) view.findViewById(R.id.STATLST_txtTitle);

        heroImage.setImageDrawable(Util.getRandomHeroImage(ctx));

        Statics.setRobotoFontLight(ctx, txtProject);
        Statics.setRobotoFontLight(ctx, btnEnd);
        Statics.setRobotoFontLight(ctx, btnStart);
        Statics.setRobotoFontLight(ctx, txtTitle);
        progressBar.setVisibility(View.GONE);
        txtTitle.setVisibility(View.GONE);
        txtProject.setVisibility(View.GONE);
        txtEmpty.setVisibility(View.GONE);
        setDates();
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = false;
                showDateDialog();
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = true;
                showDateDialog();
            }
        });


        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtCount, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        getSiteData();
                        txtCount.setAlpha(1.0f);
                    }
                });

            }
        });
        txtProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });

    }

    boolean isStartDate;
    List<ProjectSiteTaskStatusDTO> projectSiteTaskStatusList;

    public void setProjectSite(ProjectSiteDTO site) {
        this.projectSite = site;
        projectSiteTaskStatusList = new ArrayList<>();
        for (ProjectSiteTaskDTO task: projectSite.getProjectSiteTaskList()) {
            if (task.getProjectSiteTaskStatusList() != null && !task.getProjectSiteTaskStatusList().isEmpty()) {
                projectSiteTaskStatusList.addAll(task.getProjectSiteTaskStatusList());
            }
        }
        if (projectSiteTaskStatusList.isEmpty()) {
            getSiteData();
        } else {
            setList();
        }
    }
    private void getCachedSiteData() {
        progressBar.setVisibility(View.VISIBLE);
        CacheUtil.getCachedSiteData(ctx, projectSite.getProjectSiteID(), new CacheUtil.CacheSiteListener() {
            @Override
            public void onSiteReturnedFromCache(ProjectSiteDTO site) {
                progressBar.setVisibility(View.GONE);
                if (site != null) {
                    projectSite = site;
                    projectSiteTaskStatusList = projectSite.getProjectSiteTaskStatusList();
                    setList();
                } else {
                    getSiteData();
                }
            }

            @Override
            public void onDataCached() {

            }

            @Override
            public void onError() {
                Log.e(LOG,"--- no cache exists for the site, going to the cloud");
                getSiteData();
            }
        });
    }

    private void getSiteData() {
        WebCheckResult wcr = WebCheck.checkNetworkAvailability(ctx);
        if (wcr.isWifiConnected()) {
            RequestDTO w = new RequestDTO(RequestDTO.GET_SITE_STATUS);
            w.setProjectSiteID(projectSite.getProjectSiteID());
            progressBar.setVisibility(View.VISIBLE);
            WebSocketUtil.sendRequest(ctx,Statics.COMPANY_ENDPOINT,w, new WebSocketUtil.WebSocketListener() {
                @Override
                public void onMessage(final ResponseDTO response) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            if (!ErrorUtil.checkServerError(ctx, response)) {
                                return;
                            }
                            projectSite = response.getProjectSiteList().get(0);
                            projectSiteTaskStatusList = new ArrayList<>();
                            for (ProjectSiteTaskDTO task: projectSite.getProjectSiteTaskList()) {
                                if (task.getProjectSiteTaskStatusList() != null && !task.getProjectSiteTaskStatusList().isEmpty()) {
                                    projectSiteTaskStatusList.addAll(task.getProjectSiteTaskStatusList());
                                }
                            }
                            if (projectSiteTaskStatusList.isEmpty()) {
                                Util.showToast(ctx,"No status updates have been recorded.");
                            } else {
                                setList();
                                CacheUtil.cacheSiteData(ctx,projectSite, null);
                            }

                        }

                    });
                }

                @Override
                public void onClose() {

                }

                @Override
                public void onError(String message) {

                }
            });
        } else {
            Util.showToast(ctx,ctx.getString(R.string.connect_wifi));
        }
    }
    private void setList() {
        Log.d(LOG, "########## setList");
        Collections.sort(projectSiteTaskStatusList);
        txtCount.setText("" + projectSiteTaskStatusList.size());
        adapter = new StatusReportAdapter(ctx, R.layout.status_report_card, projectSiteTaskStatusList);
        listView.setAdapter(adapter);

    }

    DatePickerDialog dpStart;
    int mYear, mMonth, mDay;

    private void showDateDialog() {
        final Calendar calendar = Calendar.getInstance();
        int xYear, xMth, xDay;
        if (mYear == 0) {
            xYear = calendar.get(Calendar.YEAR);
            xMth = calendar.get(Calendar.MONTH);
            xDay = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            xYear = mYear;
            xMth = mMonth;
            xDay = mDay;
        }
        dpStart = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog,
                                          int year, int month, int day) {
                        mYear = year;
                        mMonth = month;
                        mDay = day;

                        calendar.set(Calendar.YEAR, mYear);
                        calendar.set(Calendar.MONTH, mMonth);
                        calendar.set(Calendar.DAY_OF_MONTH, mDay);

                        if (isStartDate) {
                            calendar.set(Calendar.HOUR_OF_DAY, 0);
                            calendar.set(Calendar.MINUTE,0);
                            calendar.set(Calendar.SECOND,0);
                            startDate = calendar.getTime();
                        } else {
                            endDate = calendar.getTime();
                        }
                        setDates();
                        Util.flashSeveralTimes(txtCount, 200, 2, new Util.UtilAnimationListener() {
                            @Override
                            public void onAnimationEnded() {
                                getSiteData();
                            }
                        });
                    }


                }, xYear, xMth, xDay, true
        );
        dpStart.setVibrate(true);
        dpStart.setYearRange(2013, calendar.get(Calendar.YEAR));
        Bundle args = new Bundle();
        args.putInt("year", mYear);
        args.putInt("month", mMonth);
        args.putInt("day", mDay);

        dpStart.setArguments(args);
        dpStart.show(getFragmentManager(), "diagx");


    }

    private void setDates() {
        //set dates, use week (7days) as default range
        DateTime now = new DateTime();
        DateTime then = now.minusDays(7);

        if (startDate == null) {
            then = then.withHourOfDay(0);
            then = then.withMinuteOfHour(0);
            then = then.withSecondOfMinute(0);
            startDate = then.toDate();
        }
        if (endDate == null) {
            endDate = now.toDate();
        }
        btnStart.setText(sdf.format(startDate));
        btnEnd.setText(sdf.format(endDate));

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    TaskDTO task;
    int staffType;
    public static final int OPERATIONS = 1, PROJECT_MANAGER = 2, SITE_SUPERVISOR = 3;
    static final String LOG = SiteStatusReportFragment.class.getSimpleName();

    @Override
    public void animateCounts() {
        Util.animateRotationY(txtCount, 500);

    }

}
