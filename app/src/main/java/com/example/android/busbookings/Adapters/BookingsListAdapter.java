package com.example.android.busbookings.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.android.busbookings.Objects.BookingModel;
import com.example.android.busbookings.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookingsListAdapter extends RecyclerView.Adapter<BookingsListAdapter.BookingVH>{

    DatabaseReference databaseReference;

    public void setUpdatingList(UpdatingList updatingList) {
        this.updatingList = updatingList;

    }

    UpdatingList updatingList;
    ArrayList<BookingModel> bookingList;
    Context context;

    public BookingsListAdapter(ArrayList<BookingModel> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }
    public interface UpdatingList{
        public void update();
    }

    @NonNull
    @Override
    public BookingVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BookingVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_booking,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingVH bookingVH, int i) {
        bookingVH.populateBooking(bookingList.get(i));
        System.out.println(">>>>>>>>>>>>>"+bookingList.get(i));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class BookingVH extends RecyclerView.ViewHolder
    {
        TextView from,to,date,time,seat;
        ImageButton cancel_booking;
        Button preview;

        public BookingVH(@NonNull View itemView) {
            super(itemView);
            cancel_booking= itemView.findViewById(R.id.delete_Booking);

            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            seat = itemView.findViewById(R.id.seat);

            preview = itemView.findViewById(R.id.PreviewBtn);

        }

        void populateBooking(final BookingModel bookingModel)
        {
            final int totalCost;
            final String Email;
            Email = bookingModel.getEmail();
            totalCost = bookingModel.getTotalCost();
            from.setText(bookingModel.getFrom());
            to.setText(bookingModel.getTo());
            date.setText(bookingModel.getDate());
            time.setText(bookingModel.getTime());
            seat.setText(bookingModel.getSeat());
             final String Booking_id = Email + bookingModel.getFrom() +bookingModel.getTo() + totalCost +bookingModel.getSeat();

            cancel_booking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("bookings");
                    databaseReference.keepSynced(true);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for(DataSnapshot child : dataSnapshot.getChildren())
                            {
                                if(child.child("id").getValue(String.class).equals(Booking_id))
                                {
                                    child.getRef().removeValue();
                                    updatingList.update();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("ERROR>>>>>>"+databaseError.getMessage());
                        }
                    });
                }
            });
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayTrack(bookingModel.getFrom(),bookingModel.getTo());
                }
            });
        }
        private void displayTrack(String fromDest, String toDest) {
            try {
                Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + fromDest + "/" + toDest);

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }catch(ActivityNotFoundException e){
                Uri uri =Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}
