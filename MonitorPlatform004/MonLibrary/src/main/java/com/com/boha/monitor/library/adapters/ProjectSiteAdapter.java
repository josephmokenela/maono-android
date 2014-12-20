package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.WebCheck;
import com.com.boha.monitor.library.util.WebCheckResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ProjectSiteAdapter extends ArrayAdapter<ProjectSiteDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ProjectSiteDTO> mList;
    private Context ctx;
    private Random random;
    public interface ProjectSiteListener {
        public void onProjectSiteClicked(ProjectSiteDTO site, int index);
    }

    ProjectSiteListener listener;
    public ProjectSiteAdapter(Context context, int textViewResourceId,
                              List<ProjectSiteDTO> list, ProjectSiteListener listener) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        this.listener = listener;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        TextView txtName, txtLastStatus, txtTaskName;
        TextView txtTaskCount, txtStatusCount;
        TextView txtNumber, txtDate, txtBen, txtAccuracy;
        ImageView imgHero, imgConfirmed;
        View statLayout1, statLayout2, imageScroller;
        LinearLayout imageLayout;
        ImageView img1, img2, img3,img4,img5,img6,img7,img8, img9, img10;
        TextView num1, num2,num3,num4,num5,num6,num7,num8,num9,num10;
        TextView date1, date2,date3,date4,date5,date6,date7,date8,date9,date10;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        random = new Random(System.currentTimeMillis());
        final ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();

            item.img1 = (ImageView)convertView.findViewById(R.id.SITE_scrollImage1);
            item.img2 = (ImageView)convertView.findViewById(R.id.SITE_scrollImage2);
            item.img3 = (ImageView)convertView.findViewById(R.id.SITE_scrollImage3);
            item.img4 = (ImageView)convertView.findViewById(R.id.SITE_scrollImage4);
            item.img5 = (ImageView)convertView.findViewById(R.id.SITE_scrollImage5);
            item.img6 = (ImageView)convertView.findViewById(R.id.SITE_scrollImage6);
            item.img7 = (ImageView)convertView.findViewById(R.id.SITE_scrollImage7);
            item.img8 = (ImageView)convertView.findViewById(R.id.SITE_scrollImage8);
            item.img9 = (ImageView)convertView.findViewById(R.id.SITE_scrollImage9);
            item.img10 = (ImageView)convertView.findViewById(R.id.SITE_scrollImage10);

            item.num1 = (TextView)convertView.findViewById(R.id.SITE_number1);
            item.num2 = (TextView)convertView.findViewById(R.id.SITE_number2);
            item.num3 = (TextView)convertView.findViewById(R.id.SITE_number3);
            item.num4 = (TextView)convertView.findViewById(R.id.SITE_number4);
            item.num5 = (TextView)convertView.findViewById(R.id.SITE_number5);

            item.num6 = (TextView)convertView.findViewById(R.id.SITE_number6);
            item.num7 = (TextView)convertView.findViewById(R.id.SITE_number7);
            item.num8 = (TextView)convertView.findViewById(R.id.SITE_number8);
            item.num9 = (TextView)convertView.findViewById(R.id.SITE_number9);
            item.num10 = (TextView)convertView.findViewById(R.id.SITE_number10);

            item.date1 = (TextView)convertView.findViewById(R.id.SITE_date1);
            item.date2 = (TextView)convertView.findViewById(R.id.SITE_date2);
            item.date3 = (TextView)convertView.findViewById(R.id.SITE_date3);
            item.date4 = (TextView)convertView.findViewById(R.id.SITE_date4);
            item.date5 = (TextView)convertView.findViewById(R.id.SITE_date5);

            item.date6 = (TextView)convertView.findViewById(R.id.SITE_date6);
            item.date7 = (TextView)convertView.findViewById(R.id.SITE_date7);
            item.date8 = (TextView)convertView.findViewById(R.id.SITE_date8);
            item.date9 = (TextView)convertView.findViewById(R.id.SITE_date9);
            item.date10 = (TextView)convertView.findViewById(R.id.SITE_date10);

            item.imageLayout = (LinearLayout) convertView
                    .findViewById(R.id.SITE_imageLayout);
            item.imageScroller =  convertView
                    .findViewById(R.id.SITE_imageScroll);
            item.txtName = (TextView) convertView
                    .findViewById(R.id.SITE_txtName);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.SITE_image);
            item.txtBen = (TextView) convertView
                    .findViewById(R.id.SITE_txtBeneficiary);
            item.txtTaskCount = (TextView) convertView
                    .findViewById(R.id.SITE_txtTaskCount);
            item.txtTaskName = (TextView) convertView
                    .findViewById(R.id.SITE_lastTask);
            item.statLayout1 = convertView.findViewById(R.id.SITE_bottom);
            item.statLayout2 = convertView.findViewById(R.id.SITE_layoutStatus);
            item.txtStatusCount = (TextView) convertView
                    .findViewById(R.id.SITE_txtStatusCount);
            item.txtDate = (TextView) convertView
                    .findViewById(R.id.SITE_lastStatusDate);
            item.txtLastStatus = (TextView) convertView
                    .findViewById(R.id.SITE_lastStatus);
            item.txtAccuracy = (TextView) convertView
                    .findViewById(R.id.SITE_accuracy);

            item.imgHero = (ImageView) convertView.findViewById(R.id.SITE_heroImage);
            item.imgConfirmed = (ImageView) convertView.findViewById(R.id.SITE_confirmed);
            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final ProjectSiteDTO p = mList.get(position);
        item.txtName.setText(p.getProjectSiteName());
        item.txtNumber.setText("" + (position + 1));

        if (p.getAccuracy() == null) {
            item.txtAccuracy.setVisibility(View.GONE);
        } else {
            item.txtAccuracy.setVisibility(View.VISIBLE);
            item.txtAccuracy.setText(df.format(p.getAccuracy()));
        }
        if (p.getStatusCount() != null)
            item.txtStatusCount.setText("" + p.getStatusCount());
        else
            item.txtStatusCount.setText("0");

        if (p.getBeneficiary() != null) {
            item.txtBen.setText(p.getBeneficiary().getFullName());
        } else {
            item.txtBen.setVisibility(View.GONE);
        }
        if (p.getLocationConfirmed() == null) {
            item.imgConfirmed.setVisibility(View.GONE);
        } else {
            item.imgConfirmed.setVisibility(View.VISIBLE);
        }
        Statics.setRobotoFontLight(ctx, item.txtName);
        //manage scrollview images
        hideScrollImages(item);
        int index = 0;
        boolean b1 = false,b2 = false,b3 = false,b4 = false,b5 = false,
                b6 = false,b7 = false,b8 = false,b9 = false,b10 = false;
        WebCheckResult wcr = WebCheck.checkNetworkAvailability(ctx, true);

        if (p.getPhotoUploadList() != null && !p.getPhotoUploadList().isEmpty() && wcr.isWifiConnected()) {
            item.imageScroller.setVisibility(View.VISIBLE);
            item.imgHero.setVisibility(View.GONE);
            for (final PhotoUploadDTO d : p.getPhotoUploadList()) {
                if (d.getThumbFlag() == null) {
                    continue;
                }
                final String uri = Statics.IMAGE_URL + d.getUri();
                //Log.i("ProjectSiteAdapter","## scroll uri: " + uri);
                switch (index) {
                    case 0:
                        ImageLoader.getInstance().displayImage(uri, item.img1);
                        item.img1.setVisibility(View.VISIBLE);
                        item.num1.setText("1");
                        item.num1.setVisibility(View.VISIBLE);
                        item.date1.setVisibility(View.VISIBLE);
                        item.date1.setText(sdf.format(d.getDateTaken()));
                        b1 = true;
                        break;
                    case 1:
                        ImageLoader.getInstance().displayImage(uri, item.img2);
                        item.img2.setVisibility(View.VISIBLE);
                        item.num2.setText("2");
                        item.num2.setVisibility(View.VISIBLE);
                        item.date2.setText(sdf.format(d.getDateTaken()));
                        item.date2.setVisibility(View.VISIBLE);
                        b2 = true;
                        break;
                    case 2:
                        ImageLoader.getInstance().displayImage(uri, item.img3);
                        item.img3.setVisibility(View.VISIBLE);
                        item.num3.setText("3");
                        item.num3.setVisibility(View.VISIBLE);
                        item.date3.setText(sdf.format(d.getDateTaken()));
                        item.date3.setVisibility(View.VISIBLE);
                        b3 = true;
                        break;
                    case 3:
                        ImageLoader.getInstance().displayImage(uri, item.img4);
                        item.img4.setVisibility(View.VISIBLE);
                        item.num4.setText("4");
                        item.num4.setVisibility(View.VISIBLE);
                        item.date4.setText(sdf.format(d.getDateTaken()));
                        item.date4.setVisibility(View.VISIBLE);
                        b4 = true;
                        break;
                    case 4:
                        ImageLoader.getInstance().displayImage(uri, item.img5);
                        item.img5.setVisibility(View.VISIBLE);
                        item.num5.setText("5");
                        item.num5.setVisibility(View.VISIBLE);
                        item.date5.setText(sdf.format(d.getDateTaken()));
                        item.date5.setVisibility(View.VISIBLE);
                        b5 = true;
                        break;
                    case 5:
                        ImageLoader.getInstance().displayImage(uri, item.img6);
                        item.img6.setVisibility(View.VISIBLE);
                        item.num6.setText("6");
                        item.num6.setVisibility(View.VISIBLE);
                        item.date6.setText(sdf.format(d.getDateTaken()));
                        item.date6.setVisibility(View.VISIBLE);
                        b6 = true;
                        break;
                    case 6:
                        ImageLoader.getInstance().displayImage(uri, item.img7);
                        item.img7.setVisibility(View.VISIBLE);
                        item.num7.setText("7");
                        item.num7.setVisibility(View.VISIBLE);
                        item.date7.setText(sdf.format(d.getDateTaken()));
                        item.date7.setVisibility(View.VISIBLE);

                        b7 = true;
                        break;
                    case 7:
                        ImageLoader.getInstance().displayImage(uri, item.img8);
                        item.img8.setVisibility(View.VISIBLE);
                        item.num8.setText("8");
                        item.num8.setVisibility(View.VISIBLE);
                        item.date8.setText(sdf.format(d.getDateTaken()));
                        item.date8.setVisibility(View.VISIBLE);
                        b8 = true;
                        break;
                    case 8:
                        ImageLoader.getInstance().displayImage(uri, item.img9);
                        item.img9.setVisibility(View.VISIBLE);
                        item.num9.setText("9");
                        item.num9.setVisibility(View.VISIBLE);
                        item.date9.setText(sdf.format(d.getDateTaken()));
                        item.date9.setVisibility(View.VISIBLE);
                        b9 = true;
                        break;
                    case 9:
                        ImageLoader.getInstance().displayImage(uri, item.img10);
                        item.img10.setVisibility(View.VISIBLE);
                        item.num10.setText("10");
                        item.num10.setVisibility(View.VISIBLE);
                        item.date10.setText(sdf.format(d.getDateTaken()));
                        item.date10.setVisibility(View.VISIBLE);
                        b10 = true;
                        break;
                }
                index++;
                if (index == 10) {
                    break;
                }
            }

        } else {
            hideScrollImages(item);
            item.imageScroller.setVisibility(View.GONE);
            item.imgHero.setVisibility(View.GONE);
        }

        if (!b1) {
           item.img1.setVisibility(View.GONE);
            item.date1.setVisibility(View.GONE);
        }
        if (!b2) {
            item.img2.setVisibility(View.GONE);
            item.date2.setVisibility(View.GONE);
        }
        if (!b3) {
            item.img3.setVisibility(View.GONE);
            item.date3.setVisibility(View.GONE);
        }
        if (!b4) {
            item.img4.setVisibility(View.GONE);
            item.date4.setVisibility(View.GONE);
        }
        if (!b5) {
            item.img5.setVisibility(View.GONE);
            item.date5.setVisibility(View.GONE);
        }
        if (!b6) {
            item.img6.setVisibility(View.GONE);
            item.date7.setVisibility(View.GONE);
        }
        if (!b7) {
            item.img7.setVisibility(View.GONE);
            item.date7.setVisibility(View.GONE);
        }
        if (!b8) {
            item.img8.setVisibility(View.GONE);
            item.date8.setVisibility(View.GONE);
        }
        if (!b9) {
            item.img9.setVisibility(View.GONE);
            item.date9.setVisibility(View.GONE);
        }
        if (!b10) {
            item.img10.setVisibility(View.GONE);
            item.date10.setVisibility(View.GONE);
        }

        if (p.getPhotoUploadList() != null && wcr.isWifiConnected()) {
            if (p.getPhotoUploadList().size() < 3 && p.getPhotoUploadList().size() > 0) {
                hideScrollImages(item);
                item.imageScroller.setVisibility(View.GONE);
                item.imgHero.setVisibility(View.VISIBLE);
                final String uri = Statics.IMAGE_URL + p.getPhotoUploadList().get(0).getUri();
                ImageLoader.getInstance().displayImage(uri, item.imgHero);

            }
        }

        if (p.getLastStatus() != null) {
            item.txtLastStatus.setText(p.getLastStatus().getTaskStatus().getTaskStatusName());
            item.txtTaskName.setText(p.getLastStatus().getTask().getTaskName());
            if (p.getLastStatus().getStatusDate() == null) {
                item.txtDate.setText("Date not available");
            } else {
                item.txtDate.setText(sdf.format(p.getLastStatus().getStatusDate()));
            }

            item.statLayout1.setVisibility(View.VISIBLE);
            item.statLayout2.setVisibility(View.VISIBLE);
            switch (p.getLastStatus().getTaskStatus().getStatusColor()) {
                case TaskStatusDTO.STATUS_COLOR_GREEN:
                    item.txtStatusCount.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval));
                    item.txtTaskCount.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval_small));
                    break;
                case TaskStatusDTO.STATUS_COLOR_YELLOW:
                    item.txtStatusCount.setBackground(ctx.getResources().getDrawable(R.drawable.xorange_oval));
                    item.txtTaskCount.setBackground(ctx.getResources().getDrawable(R.drawable.xorange_oval_small));
                    break;
                case TaskStatusDTO.STATUS_COLOR_RED:
                    item.txtStatusCount.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval));
                    item.txtTaskCount.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
                    break;
                default:
                    item.txtStatusCount.setBackground(ctx.getResources().getDrawable(R.drawable.xgrey_oval));
                    item.txtTaskCount.setBackground(ctx.getResources().getDrawable(R.drawable.xgrey_oval_small));
                    break;
            }

        } else {
            item.statLayout1.setVisibility(View.GONE);
            item.statLayout2.setVisibility(View.GONE);
            item.txtStatusCount.setBackground(ctx.getResources().getDrawable(R.drawable.xgrey_oval));
            item.txtTaskCount.setBackground(ctx.getResources().getDrawable(R.drawable.xgrey_oval_small));
        }

        if (p.getProjectSiteTaskList() == null) {
            item.txtTaskCount.setText("0");
        } else {
            item.txtTaskCount.setText("" + p.getProjectSiteTaskList().size());
        }

        if (p.getLastStatus() != null) {
            if (p.getLastStatus().getStatusDate() == null) {
                item.txtDate.setText("Date not available");
            } else {
                item.txtDate.setText(sdf.format(p.getLastStatus().getStatusDate()));
                item.txtDate.setVisibility(View.VISIBLE);
            }
        }

        item.imageScroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProjectSiteClicked(p,position);
            }
        });
        item.imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProjectSiteClicked(p,position);
            }
        });
        item.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProjectSiteClicked(p,position);
            }
        });
        item.txtStatusCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProjectSiteClicked(p,position);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProjectSiteClicked(p,position);
            }
        });
        Statics.setRobotoFontLight(ctx, item.txtNumber);
        Statics.setRobotoFontBold(ctx, item.txtDate);
        Statics.setRobotoFontLight(ctx, item.txtName);

        return (convertView);
    }

    private void hideScrollImages(ViewHolderItem item) {
        item.img1.setVisibility(View.GONE);
        item.img2.setVisibility(View.GONE);
        item.img3.setVisibility(View.GONE);
        item.img4.setVisibility(View.GONE);
        item.img5.setVisibility(View.GONE);
        item.img6.setVisibility(View.GONE);
        item.img7.setVisibility(View.GONE);
        item.img8.setVisibility(View.GONE);
        item.img9.setVisibility(View.GONE);
        item.img10.setVisibility(View.GONE);

        item.num1.setVisibility(View.GONE);
        item.num2.setVisibility(View.GONE);
        item.num3.setVisibility(View.GONE);
        item.num4.setVisibility(View.GONE);
        item.num5.setVisibility(View.GONE);
        item.num6.setVisibility(View.GONE);
        item.num7.setVisibility(View.GONE);
        item.num8.setVisibility(View.GONE);
        item.num9.setVisibility(View.GONE);
        item.num10.setVisibility(View.GONE);

    }

    static final Locale x = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.00");
}
