package com.appdazzle_innovations.testingcenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsHolder>{

    private Context context;
    private List<String> name;
    private List<String> number;
    private List<String> email;
    private List<String> role;
    private List<Integer> picture;
    public ContactsAdapter(Context context, List<String> name, List<String> number, List<String> email, List<String> role, List<Integer> picture){
        this.context = context;
        this.context = context;
        this.name = name;
        this.email = email;
        this.number = number;
        this.role = role;
        this.picture = picture;
    }

    @NonNull
    @Override
    public ContactsHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.contactslayout, parent, false);
        return new ContactsHolder(v);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ContactsHolder holder, int position) {
        holder.contactname.setText(name.get(position));
        holder.contactrole.setText(role.get(position));
        holder.contactnumber.setText(number.get(position));
        holder.contactemail.setText(email.get(position));
        holder.contactimage.setImageResource(picture.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public static class ContactsHolder extends RecyclerView.ViewHolder{
        ImageView contactimage;
        TextView contactname,contactrole,contactnumber,contactemail;
        public ContactsHolder (@NonNull View itemView){
            super(itemView);
            contactimage = itemView.findViewById(R.id.contactimage);
            contactname = itemView.findViewById(R.id.contactname);
            contactemail = itemView.findViewById(R.id.contactemail);
            contactnumber = itemView.findViewById(R.id.contactnumber);
            contactrole = itemView.findViewById(R.id.contactrole);
        }
    }
}
