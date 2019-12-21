package com.demo.ipc.use.file;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.ipc.R;
import com.demo.ipc.model.Book;

import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2019/11/14 14:51
 * description :
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private List<Book> books;

    public BookAdapter(List<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_book, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Book book = books.get(i);
        viewHolder.tx.setText(book.getId() + ":" + book.getName());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void clearAndAddAll(List<Book> data) {
        if (books != null) {
            books.clear();
            books.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (books != null) {
            books.clear();
            notifyDataSetChanged();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tx;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tx = (TextView) itemView;
        }
    }

}
