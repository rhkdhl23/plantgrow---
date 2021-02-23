package com.example.realreal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.realreal.Plant;
import com.example.realreal.R;
import com.example.realreal.activity.AddActivity;
import com.example.realreal.activity.ViewActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Plant> plantList;
    private Context context;
    private FirebaseStorage storage;

    public MyRecyclerViewAdapter(List<Plant> plantList, Context context) {
        this.plantList = plantList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new RowCell(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final Plant plant = plantList.get(position);
        ((RowCell)holder).name.setText(plant.getName());
        ((RowCell)holder).kind.setText(plant.getKind());
        Glide.with(((RowCell) holder).imageView.getContext()).load(plant.getImageUrl()).into(((RowCell) holder).imageView);

        ((RowCell)holder).imageView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ViewActivity.class);
            intent.putExtra("name", plant.getName());
            intent.putExtra("kind", plant.getKind());
            intent.putExtra("intro", plant.getIntro());
            intent.putExtra("imageUrl", plant.getImageUrl());
            intent.putExtra("uid", plant.getUid());
            intent.putExtra("key", plant.getKey());
            context.startActivity(intent);
        });

        ((RowCell)holder).imageButton.setOnClickListener(view -> {
            String[] items = {"식물수정", "식물삭제", "취소"};
            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setTitle("");

            // 목록 클릭시 설정
            ab.setItems(items, (dialog, index) -> {
                if(index == 0) {
                    Intent intent = new Intent(context, AddActivity.class);
                    intent.putExtra("FLAG", "U");
                    intent.putExtra("name", plant.getName());
                    intent.putExtra("kind", plant.getKind());
                    intent.putExtra("intro", plant.getIntro());
                    intent.putExtra("imageUrl", plant.getImageUrl());
                    intent.putExtra("uid", plant.getUid());
                    intent.putExtra("key", plant.getKey());
                    context.startActivity(intent);
                } else if(index == 1) {
                    deletePlant(position);
                }

                dialog.dismiss();
            });

            ab.show();
        });
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    private class RowCell extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name;
        private TextView kind;
        private ImageButton imageButton;

        private RowCell(View view) {
            super(view);
            imageView = view.findViewById(R.id.cardview_imageview);
            name = view.findViewById(R.id.cardview_name);
            kind = view.findViewById(R.id.cardview_kind);
            imageButton = view.findViewById(R.id.cardview_btn);
        }
    }

    private void deletePlant(final int position) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        String name = plantList.get(position).getName();
        ab.setTitle(name + "을(를) 삭제하시겠습니까?");
        String[] items = {"예", "아니오"};

        // 목록 클릭시 설정
        ab.setItems(items, (dialog, index) -> {
            if(index == 0)
                deletePlantProcess(position);

            dialog.dismiss();
        });

        ab.show();
    }

    private void deletePlantProcess(final int position) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = plantList.get(position).getKey();

        database.getReference().child("plant").child(key).removeValue().addOnSuccessListener(aVoid -> {
            String imageName = plantList.get(position).getImageName();
            storage = FirebaseStorage.getInstance();
            storage.getReference().child("images").child(imageName).delete().addOnSuccessListener(aVoid1 -> {
                Toast.makeText(context, "식물이 삭제됐습니다.", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Toast.makeText(context, "식물 삭제 됐습니다..", Toast.LENGTH_SHORT).show());
        }).addOnFailureListener(e -> Toast.makeText(context, "식물 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show());
    }
}
