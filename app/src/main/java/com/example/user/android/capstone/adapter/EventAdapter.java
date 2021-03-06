package com.example.user.android.capstone.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.android.capstone.R;
import com.example.user.android.capstone.activity.ChatActivity;
import com.example.user.android.capstone.activity.EventInfoActivity;
import com.example.user.android.capstone.activity.MainActivity;
import com.example.user.android.capstone.activity.UserChatsActivity;
import com.example.user.android.capstone.model.Event;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by nataliakuleniuk on 6/26/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    List<Event> events;
    Context context;
    Class currentActivity;

    public EventAdapter(Context context, List<Event> events, Class currentActivityClass) {
        this.events = removeDuplicates(events);
        this.context = context;
        this.currentActivity = currentActivityClass;
    }

    private List<Event> removeDuplicates(List<Event> userEvents) {
        List<Event> temp = new ArrayList<Event>();
        for (int i = 0; i < userEvents.size(); i++) {
            if (!temp.contains(userEvents.get(i))) {
                temp.add(userEvents.get(i));
            }
        }
        return temp;
    }


    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new EventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, final int position) {
        holder.eventTitleTextView.setText(events.get(position).getTitle());
        if (context.getClass() == MainActivity.class) {
            setImage(holder.iV, events.get(position).getSportCategory());
            holder.eventDateTextView.setText(events.get(position).getDate());
            String address = events.get(position).getAddress();
            LatLng location = getLocationFromAddress(address);
            if (location != null) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String cityName = addresses.get(0).getAddressLine(1);
                String[] addressValues = cityName.split(",");
                holder.eventLocationTextView.setText(addressValues[0]);

            }
        } else {
            holder.layoutLinear.setVisibility(View.GONE);
            holder.eventTitleTextView.setTextColor(Color.parseColor("#212121"));
            holder.iV.setVisibility(View.GONE);
            holder.eventTitleTextView.setTypeface(null, Typeface.NORMAL);
            holder.eventTitleTextView.setTextSize(20);
            holder.eventTitleTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        holder.eventLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentActivity != null && currentActivity == UserChatsActivity.class) {
                    view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    view.findViewById(R.id.new_message_icon).setVisibility(View.GONE);
                    Intent intentToOpenChat = new Intent(context, ChatActivity.class);
                    intentToOpenChat.putExtra("event", (Parcelable) events.get(position));
                    context.startActivity(intentToOpenChat);
                } else {
                    Class destinationClass = EventInfoActivity.class;
                    Intent intentToStartEventInfoActivity = new Intent(context, destinationClass);
                    intentToStartEventInfoActivity.putExtra("event", (Parcelable) events.get(position));
                    intentToStartEventInfoActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentToStartEventInfoActivity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        LinearLayout eventLinearLayout;
        TextView eventTitleTextView;
        TextView eventLocationTextView;
        TextView eventDateTextView;
        LinearLayout layoutLinear;
        ImageView iV;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            eventDateTextView = (TextView) itemView.findViewById(R.id.event_date);
            eventLocationTextView = (TextView) itemView.findViewById(R.id.event_location);
            eventTitleTextView = (TextView) itemView.findViewById(R.id.event_sport_title);
            eventLinearLayout = (LinearLayout) itemView.findViewById(R.id.layout_item);
            layoutLinear = (LinearLayout) itemView.findViewById(R.id.date_city_layout);
            iV = (ImageView) itemView.findViewById(R.id.event_photo);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;

        LatLng p1 = null;
        try {
            if (strAddress != null) {
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null || address.size() == 0) {
                    return null;
                }
                double latit = address.get(0).getLatitude();
                double longit = address.get(0).getLongitude();
                p1 = new LatLng(latit, longit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p1;
    }


    public static void setImage(final View view, String sportCategory) {
        switch (sportCategory) {
            case "Tennis":
                view.setBackgroundResource(R.drawable.tennis);
                break;
            case "Football":
                view.setBackgroundResource(R.drawable.football);
                break;
            case "Boxing":
                view.setBackgroundResource(R.drawable.boxing);
                break;
            case "Skiing":
                view.setBackgroundResource(R.drawable.skiing);
                break;
            case "Cycling":
                view.setBackgroundResource(R.drawable.cycling);
                break;
            case "Running":
                view.setBackgroundResource(R.drawable.running);
                break;
            case "Ice Hockey":
                view.setBackgroundResource(R.drawable.hockey);
                break;
            case "Hiking":
                view.setBackgroundResource(R.drawable.hiking);
                break;
            case "Climbing":
                view.setBackgroundResource(R.drawable.climbing);
                break;
            case "Gym Workout":
                view.setBackgroundResource(R.drawable.workout);
                break;
            case "Swimming":
                view.setBackgroundResource(R.drawable.swimming);
                break;
            case "Snowboarding":
                view.setBackgroundResource(R.drawable.snowboarding);
                break;
            case "Skateboarding":
                view.setBackgroundResource(R.drawable.skateboarding);
                break;
            case "Volleyball":
                view.setBackgroundResource(R.drawable.volleyball);
                break;
            case "Basketball":
                view.setBackgroundResource(R.drawable.basketbal);
                break;
            case "Bowling":
                view.setBackgroundResource(R.drawable.bowling);
                break;
            case "Golf":
                view.setBackgroundResource(R.drawable.golf);
                break;
            case "Baseball":
                view.setBackgroundResource(R.drawable.baseball);
                break;
            case "Soccer":
                view.setBackgroundResource(R.drawable.soccer);
                break;
            case "Other":
                view.setBackgroundResource(R.drawable.default_sport1);
                break;
            default:
                view.setBackgroundResource(R.drawable.default_sport1);
                break;
        }

    }

    public void filter(List<Event> newList) {
        this.events = new ArrayList<>();
        this.events.addAll(newList);
        notifyDataSetChanged();
    }


}


