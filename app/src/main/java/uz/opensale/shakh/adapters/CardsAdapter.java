package uz.opensale.shakh.adapters;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import uz.opensale.shakh.R;
import uz.opensale.shakh.models.Cards;

/**
 * Created by shakh on 02.03.18.
 */

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {
    public static final String KEY_ID = "id";
    public static final String KEY_BANK_ID = "bank_id";
    public static final String KEY_BANK_NAME = "bank_name";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_EXP_DATE = "exp_date";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_NAME = "name";
    public static final String KEY_KEY = "key";

    private List<Cards> cardsList;
    private Context context;

    public CardsAdapter(List<Cards> cardsList, Context context){
        this.cardsList = cardsList;
        this.context = context;
    }

    @Override
    public int getItemCount(){
        return cardsList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_items, parent, false);

        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        final Cards card = cardsList.get(position);
        holder.name.setText(card.getName());
        holder.cash.setText(String.valueOf(card.getCash()) + " UZS");
        holder.number.setText(card.getNumber());
        holder.expdate.setText("Срок: " + card.getExp_date());

        holder.card_contextMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.card_contextMenu);
                popup.inflate(R.menu.card_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.card_menu_activate:
                                Toast.makeText(context, "Activate", Toast.LENGTH_LONG).show();
                        }

                        return false;
                    }
                });

                popup.show();
            }
        });

       /* holder.card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, String.valueOf(card.getId()), Toast.LENGTH_LONG).show();
            }
        });*/
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        TextView cash;
        public TextView number;
        TextView expdate;
        LinearLayout card_item;
        TextView card_contextMenu;

        ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.card_name);
            cash = (TextView) itemView.findViewById(R.id.card_cash);
            number = (TextView) itemView.findViewById(R.id.card_number);
            expdate = (TextView) itemView.findViewById(R.id.card_expdate);
            card_item = (LinearLayout) itemView.findViewById(R.id.card_item);
            card_contextMenu = itemView.findViewById(R.id.card_contextMenu);
        }


    }
}
