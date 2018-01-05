package org.btssio.slam.facecast.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.btssio.slam.facecast.Class.Offre;
import org.btssio.slam.facecast.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SATTIA Dhinesh on 10/12/2017.
 */

public class OffreAdapter  extends ArrayAdapter<Offre>{


    public OffreAdapter(@NonNull Context context, @NonNull List<Offre> objects) {
        super(context, 0, objects);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row, parent, false);

        }


        Offre offre = getItem(position);
        TextView identite = (TextView) convertView.findViewById(R.id.tvIdentiteAdapter);
       TextView email = (TextView)convertView.findViewById(R.id.tvEmailAdapter);
       TextView date = (TextView)convertView.findViewById(R.id.tvDateAdapter);

       identite.setText(offre.getNomEvenement());
       email.setText(offre.getRole());
       date.setText(offre.getDateDebut().substring(0,10));
       return convertView;

    }


}
