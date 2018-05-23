package com.beanu.l3_gensee.fragement;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.l3_gensee.R;
import com.gensee.player.Player;
import com.gensee.view.GSDocViewGx;

@SuppressLint("ValidFragment")
public class DocFragment extends Fragment {

    private Player mPlayer;
    private View mView;
    private GSDocViewGx mGlDocView;

    public DocFragment(Player player) {

        this.mPlayer = player;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.imdoc, null);
        mGlDocView = (GSDocViewGx) mView.findViewById(R.id.imGlDocView);
        mPlayer.setGSDocViewGx(mGlDocView);

        mGlDocView.setBackgroundColor(getResources().getColor(R.color.white));
        mGlDocView.setDefImg(BitmapFactory.decodeResource(getResources(), R.drawable.icon_doc_def), false);

//		AnnoFreepenEx.setFreepenExDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.freepen_ex)));
//		AnnoPointerEx.setPointerCircleDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.anno_pointer)));
//		AnnoPointerEx.setPointerCrossDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.anno_arrow)));
        return mView;

    }
}
