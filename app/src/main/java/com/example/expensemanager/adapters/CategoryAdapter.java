package com.example.expensemanager.adapters;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.CategoryItemBinding;
import com.example.expensemanager.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    Context context;
    List<Category> categoryList;

    public interface CategoryClickListener{void onCategoryItemClick(Category category);}

    CategoryClickListener categoryClickListener;

    public CategoryAdapter(Context context, List<Category> categoryList, CategoryClickListener categoryClickListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.categoryClickListener = categoryClickListener;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.binding.categoryImg.setImageResource(category.getCategoryImage());
        holder.binding.categoryTitle.setText(category.getCategoryName());
        holder.binding.categoryImg.setBackgroundTintList(context.getColorStateList(category.getCategoryColor()));
        holder.itemView.setOnClickListener(v->{
            categoryClickListener.onCategoryItemClick(category);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CategoryItemBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CategoryItemBinding.bind(itemView);
        }
    }
}
