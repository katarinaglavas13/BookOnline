package com.bookonline.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bookonline.R;
import com.bookonline.model.BookAPI;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    private ArrayList<BookAPI> books;

    public BooksAdapter(ArrayList<BookAPI> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int startIndex = position * 2;
        int endIndex = Math.min(startIndex + 1, books.size() - 1);

        holder.bind(books.get(startIndex), (endIndex >= 0) ? books.get(endIndex) : null);
    }

    @Override
    public int getItemCount() {
        // Računanje broja redova
        return (int) Math.ceil((double) books.size() / 2);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewBook1;
        private ImageView imageViewBook2;
        private TextView textViewTitle1;
        private TextView textViewTitle2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewBook1 = itemView.findViewById(R.id.imageViewBook1);
            imageViewBook2 = itemView.findViewById(R.id.imageViewBook2);
            textViewTitle1 = itemView.findViewById(R.id.textViewTitle1);
            textViewTitle2 = itemView.findViewById(R.id.textViewTitle2);
        }

        public void bind(BookAPI book1, BookAPI book2) {
            if (book1 != null) {
                Picasso.get().load(book1.getImageUrl()).into(imageViewBook1);
                textViewTitle1.setText(book1.getTitle());
            } else {
                // Ako nema knjige, sakrij prikaz za prvu karticu
                imageViewBook1.setVisibility(View.GONE);
                textViewTitle1.setVisibility(View.GONE);
            }

            if (book2 != null) {
                Picasso.get().load(book2.getImageUrl()).into(imageViewBook2);
                textViewTitle2.setText(book2.getTitle());
            } else {
                // Ako nema knjige, sakrij prikaz za drugu karticu
                imageViewBook2.setVisibility(View.GONE);
                textViewTitle2.setVisibility(View.GONE);
            }
        }
    }
}
