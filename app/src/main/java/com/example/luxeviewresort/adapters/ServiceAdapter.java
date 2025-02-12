package com.example.luxeviewresort.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luxeviewresort.R;
import com.example.luxeviewresort.models.Service;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private Context context;
    private List<Service> serviceList;
    private OnServiceClickListener onServiceClickListener;

    public interface OnServiceClickListener {
        void onServiceClick(int serviceId);
    }

    public ServiceAdapter(Context context, List<Service> serviceList, OnServiceClickListener listener) {
        this.context = context;
        this.serviceList = serviceList;
        this.onServiceClickListener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.serviceName.setText(service.getName());
        holder.servicePrice.setText("$" + service.getPrice());

        holder.btnReserve.setOnClickListener(v -> onServiceClickListener.onServiceClick(service.getId()));
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName, servicePrice;
        Button btnReserve;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            btnReserve = itemView.findViewById(R.id.btnReserve);
        }
    }
}