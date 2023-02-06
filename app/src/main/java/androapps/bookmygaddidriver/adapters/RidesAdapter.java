package androapps.bookmygaddidriver.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androapps.bookmygaddidriver.R;
import androapps.bookmygaddidriver.activities.AcceptedRideDetailsActivity;
import androapps.bookmygaddidriver.activities.Ride_Details;
import androapps.bookmygaddidriver.models.RidesDetails;
import androapps.bookmygaddidriver.utils.Methods;

/**
 * Created by ADMIN on 02-08-2016.
 */
public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {

    private ArrayList<RidesDetails> ridesDetailses;
    private Context context;
    private String type;
    String checkridestatus="0";

    public RidesAdapter(Context context, ArrayList<RidesDetails> ridesDetailses,String type) {
        this.ridesDetailses = ridesDetailses;
        this.context = context;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rides, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RidesAdapter.ViewHolder holder, final int position) {


//        if((ridesDetailses.get(position).getVehicle_category()!="1") || (ridesDetailses.get(position).getVehicle_category()!="2") ||(ridesDetailses.get(position).getVehicle_category()!="3")){
//            holder.txtrideType.setText("Ride Type : "+"Short/Pickup");
//        }
//        else if((ridesDetailses.get(position).getVehicle_category()!="2") || (ridesDetailses.get(position).getVehicle_category()!="3") ||(ridesDetailses.get(position).getVehicle_category()!="4")){
//
//            holder.txtrideType.setText("Ride Type : "+"Outstaion");
//
//        }else if((ridesDetailses.get(position).getVehicle_category()!="4") || (ridesDetailses.get(position).getVehicle_category()!="1") ||(ridesDetailses.get(position).getVehicle_category()!="3")){
//
//            holder.txtrideType.setText("Ride Type : "+"Local");
//
//        }else if((ridesDetailses.get(position).getVehicle_category()!="1") || (ridesDetailses.get(position).getVehicle_category()!="2") ||(ridesDetailses.get(position).getVehicle_category()!="4")){
//
//            holder.txtrideType.setText("Ride Type : "+"Airport");
//        }


        holder.txt_name.setText(ridesDetailses.get(position).getName());

        holder.txt_source.setText(ridesDetailses.get(position).getSource());
        holder.txt_destination.setText(ridesDetailses.get(position).getDestination());
        holder.txt_date.setText(Methods.Converttodate(ridesDetailses.get(position).getAdded_date()));
        holder.txtBookingid.setText("Booking Id : "+ridesDetailses.get(position).getBooking_id());



        if(type.equals("Newride")){


            if(ridesDetailses.get(position).getRide_status().equals("0"))
            holder.txt_status.setText("Ride Status : "+"Schedule");

            else if(ridesDetailses.get(position).getRide_status().equals("1")){

                holder.txt_status.setText("Ride Status : "+"Allocated");

            }else if(ridesDetailses.get(position).getRide_status().equals("2")){

                holder.txt_status.setText("Ride Status : "+"Ride Started");
                holder.txt_status.setTextColor(ContextCompat.getColor(context,R.color.first));

            }else if(ridesDetailses.get(position).getRide_status().equals("3")){

                holder.txt_status.setText("Ride Status : "+"Ride stopped");

            }else if(ridesDetailses.get(position).getRide_status().equals("4")){

                holder.txt_status.setText("Ride Status : "+"Canceled");

            }


            if ((ridesDetailses.get(position).getAirport_type().equals("1"))){
                holder.txtairporttype.setVisibility(View.VISIBLE);
                holder.txtairporttype.setText("Airport type : "+"Domestic");
            }
            else if(ridesDetailses.get(position).getAirport_type().equals("2")){
                holder.txtairporttype.setVisibility(View.VISIBLE);
                holder.txtairporttype.setText("Airport type : "+"International");
            }

            else if(ridesDetailses.get(position).getAirport_type().equals("")){
                holder.txtairporttype.setVisibility(View.GONE);

            }

        }
        else if(type.equals("Canceled")){


            if(ridesDetailses.get(position).getRide_type()!=null && ridesDetailses.get(position).getRide_type().equals("1"))
            {
                holder.txtrideType.setText("Ride Type : "+"Outstation");

            }
            else if(ridesDetailses.get(position).getRide_type()!=null && ridesDetailses.get(position).getRide_type().equals("2")){

                holder.txtrideType.setText("Ride Type : "+"Local");

            }else if(ridesDetailses.get(position).getRide_type()!=null && ridesDetailses.get(position).getRide_type().equals("3")){

                holder.txtrideType.setText("Ride Type : "+"Airport");

            }else if(ridesDetailses.get(position).getRide_type()!=null && ridesDetailses.get(position).getRide_type().equals("4")){

                holder.txtrideType.setText("Ride Type : "+"Short Pickup/Drop");

            }
            holder.txt_status.setText("Ride Status : "+"Canceled");


            if ((ridesDetailses.get(position).getAirport_type().equals("1"))){
                holder.txtairporttype.setVisibility(View.VISIBLE);
                holder.txtairporttype.setText("Airport type : "+"Domestic");
            }
            else if(ridesDetailses.get(position).getAirport_type().equals("2")){
                holder.txtairporttype.setVisibility(View.VISIBLE);
                holder.txtairporttype.setText("Airport type : "+"International");
            }

            else if(ridesDetailses.get(position).getAirport_type().equals("")){
                holder.txtairporttype.setVisibility(View.GONE);

            }

//            holder.lnr_main.setOnClickListener(null);
           // holder.txt_status.setVisibility(View.GONE);

    }


        else if(type.equals("completed")){
            holder.txt_status.setText("Ride Status : "+"Completed");

            if ((ridesDetailses.get(position).getAirport_type().equals("1"))){
                holder.txtairporttype.setVisibility(View.VISIBLE);
                holder.txtairporttype.setText("Airport type : "+"Domestic");
            }
            else if(ridesDetailses.get(position).getAirport_type().equals("2")){
                holder.txtairporttype.setVisibility(View.VISIBLE);
                holder.txtairporttype.setText("Airport type : "+"International");
            }

            else if(ridesDetailses.get(position).getAirport_type().equals("")){
                holder.txtairporttype.setVisibility(View.GONE);

            }


        }




        if(ridesDetailses.get(position).getVehicle_category()!=null && ridesDetailses.get(position).getVehicle_category().equals("1"))
        {
            holder.txtrideType.setText("Ride Type : "+"Outstation");

        }
        else if(ridesDetailses.get(position).getVehicle_category()!=null && ridesDetailses.get(position).getVehicle_category().equals("2")){

            holder.txtrideType.setText("Ride Type : "+"Local");

        }else if(ridesDetailses.get(position).getVehicle_category()!=null && ridesDetailses.get(position).getVehicle_category().equals("3")){

            holder.txtrideType.setText("Ride Type : "+"Airport");

        }else if(ridesDetailses.get(position).getVehicle_category()!=null && ridesDetailses.get(position).getVehicle_category().equals("4")){

            holder.txtrideType.setText("Ride Type : "+"Short Pickup/Drop");

        }else{

        }






//        holder.lnr_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(type.equals("Newride")) {
//                    Intent intent = new Intent(context, AcceptedRideDetailsActivity.class);
//                    intent.putExtra("Ride", ridesDetailses.get(position));
//                    intent.putExtra("bookingid", ridesDetailses.get(position).getBooking_id());
//                    intent.putExtra("type", ridesDetailses.get(position).getVehicle_category());
//                    intent.putExtra("status", ridesDetailses.get(position).getRide_status());
//                    intent.setAction("Adapter");
////                    intent.setAction("status");
//                    ((Activity) context).startActivity(intent);
//                }  else if(type.equals("Canceled")){
//                    String bkid= ridesDetailses.get(position).getBooking_id();
//                       Intent intent=new Intent(context, Ride_Details.class);
//                       intent.putExtra("bookingid",bkid);
//                        context.startActivity(intent);
//
//                }
//            }
//        });



        holder.lnr_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equals("Newride")) {


                    if (ridesDetailses.get(position).getRide_status().equals("2")) {

                        Intent intent = new Intent(context, AcceptedRideDetailsActivity.class);
                        intent.putExtra("Ride", ridesDetailses.get(position));
                        intent.putExtra("status", ridesDetailses.get(position).getRide_status());
                        intent.setAction("status_started");

                        ((Activity) context).startActivity(intent);
                    } else if (type.equals("Newride")) {
                        Intent intent = new Intent(context, AcceptedRideDetailsActivity.class);
                        intent.putExtra("Ride", ridesDetailses.get(position));
                        intent.putExtra("bookingid", ridesDetailses.get(position).getBooking_id());
                        intent.putExtra("type", ridesDetailses.get(position).getVehicle_category());
                        intent.putExtra("status", ridesDetailses.get(position).getRide_status());
                        intent.setAction("Adapter");
//                    intent.setAction("status");
                        ((Activity) context).startActivity(intent);
                    }


               }

                else if(type.equals("completed")){
                    String bkid= ridesDetailses.get(position).getBooking_id();
                    Intent intent=new Intent(context, Ride_Details.class);
                    intent.putExtra("bookingid",bkid);
                    context.startActivity(intent);

                }
            }

        });






    }

    @Override
    public int getItemCount() {
        return ridesDetailses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_source,txt_name,txt_destination,txt_date,txtBookingid,txtrideType,txt_status,txtairporttype;
        final  LinearLayout  lnr_main;
        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            txt_source= (TextView) itemView.findViewById(R.id.txt_source);
            txt_name= (TextView) itemView.findViewById(R.id.txt_name);
            txt_destination= (TextView) itemView.findViewById(R.id.txt_destination);
            txt_date= (TextView) itemView.findViewById(R.id.txt_date);
            txtBookingid= (TextView) itemView.findViewById(R.id.txtBookingid);
            txtrideType= (TextView) itemView.findViewById(R.id.txtrideType);
            txt_status= (TextView) itemView.findViewById(R.id.txt_status);
            txtairporttype= (TextView) itemView.findViewById(R.id.txtairporttype);
             lnr_main= (LinearLayout) itemView.findViewById(R.id.lnr_main);
        }

    }
}
