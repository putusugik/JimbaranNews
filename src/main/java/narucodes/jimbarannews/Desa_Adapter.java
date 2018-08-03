package narucodes.jimbarannews;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import narucodes.jimbarannews.Admin.ImageLoader;

/**
 * Created by User on 7/31/2018.
 */

public class Desa_Adapter extends BaseAdapter implements Filterable {

    private Activity activity;
    private ArrayList<HashMap<String,String>> data;
    private ArrayList<HashMap<String,String>> dataBackup;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    HashMap<String,String> histori = new HashMap<String, String>();

    public Desa_Adapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        dataBackup = (ArrayList<HashMap<String, String>>)data.clone();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (view == null)
            v = inflater.inflate(R.layout.desa_adapter, null);
            TextView t_judul = (TextView)v.findViewById(R.id.juduldesa);
            TextView t_info = (TextView)v.findViewById(R.id.infodesa);
            TextView t_o = (TextView)v.findViewById(R.id.desa);
            ImageView img = (ImageView)v.findViewById(R.id.imgdes);

            histori = data.get(i);

            t_judul.setText(histori.get(Main_Desa.TAG_JUDUL));
            t_info.setText(histori.get(Main_Desa.TAG_INFO));
            t_o.setText(histori.get(Main_Desa.TAG_ID));
            Picasso.with(activity.getApplicationContext()).load(histori.get(Main_Desa.TAG_GAMBAR)).into(img);

        return v;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                charSequence = charSequence.toString().toLowerCase();
                if (charSequence == null || charSequence.length()==0){
                    results.values = dataBackup;
                    results.count = dataBackup.size();
                }
                if (charSequence != null && charSequence.toString().length()>0){
                    ArrayList<HashMap<String, String>> x = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i <data.size();i++){
                        if (dataBackup.get(i).get("judul_desa").toLowerCase().contains(charSequence));
                        x.add(dataBackup.get(i));
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data = (ArrayList<HashMap<String, String>>)results.values;
                notifyDataSetChanged();
            }
        };
    }
}
