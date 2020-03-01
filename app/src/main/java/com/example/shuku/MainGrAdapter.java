package com.example.shuku;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.zip.Inflater;

public class MainGrAdapter extends RecyclerView.Adapter<MainGrAdapter.ShukuBlunk> {
    public MainGrAdapter(Context mContext, List<ChunkBean> data) {
        this.mContext = mContext;
        this.mdata=data;
    }

    private Context mContext;
    private List<ChunkBean> mdata;
    public ShukuBlunk onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShukuBlunk(LayoutInflater.from(mContext).inflate(R.layout.chunk_adapter,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ShukuBlunk holder, final int position) {
        final ChunkBean bean=mdata.get(position);
        //从bean中
        holder.chunk.setText(bean.getNum());
        holder.chunk.setHeight(holder.chunk.getWidth());
        holder.chunk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果输入的不是正整数进行提示
                if (s.length()>=2) {
                    Toast.makeText(mContext,"无效输入",Toast.LENGTH_SHORT).show();
                }
                bean.setNum(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (bean.getNum()!=null) {
                    int x=position%9;
                    int y=8-position/9;
                    bean.setLocation(new int[]{x,y});
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 81;
    }

    class ShukuBlunk extends RecyclerView.ViewHolder{
        private TextView chunk;
        public ShukuBlunk(@NonNull View itemView) {
            super(itemView);
            chunk=itemView.findViewById(R.id.ed_chunk);
        }
    }
}
