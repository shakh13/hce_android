package uz.opensale.shakh.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import uz.opensale.shakh.R;
import uz.opensale.shakh.models.History;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<History> historyList;
    private Context context;

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    public HistoryAdapter(List<History> historyList, Context context){
        this.historyList = historyList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_items, parent, false);
        pref = (SharedPreferences) context.getSharedPreferences("ssh13", 0);
        editor = (SharedPreferences.Editor) pref.edit();
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final History history = historyList.get(position);

        holder.terminal_name.setText(history.getTerminal_name());
        holder.card_number.setText(history.getCard_number());
        holder.uzs.setText("-"+String.valueOf(history.getUzs())+" USZ");
        holder.created_at.setText(history.getCreated_at());

        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "Long Click", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView terminal_name;
        public TextView card_number;
        public TextView uzs;
        public RelativeLayout item;
        public TextView created_at;

        public ViewHolder(View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.history_item);
            terminal_name = itemView.findViewById(R.id.history_terminal_name);
            card_number = itemView.findViewById(R.id.history_card_number);
            uzs = itemView.findViewById(R.id.history_uzs);
            created_at = itemView.findViewById(R.id.history_created_at);
        }
    }
}
