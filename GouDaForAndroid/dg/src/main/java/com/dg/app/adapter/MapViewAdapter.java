package com.dg.app.adapter;

import android.graphics.Bitmap;

import com.baidu.mapapi.map.Marker;
import com.dg.app.bean.MapList;
import com.dg.app.bean.MapNodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by czh on 2015/9/8.
 */
public class MapViewAdapter {
   private MapList mapList;

   public MapViewAdapter(MapList mapList)
   {
       this.mapList=mapList;

   }
    public List<Marker> getMarkers()
    {
        List<Marker> markers=new ArrayList<Marker>();
        List<MapNodes> mapNodes=mapList.getList();
       for(int i=0;i<mapNodes.size();i++)
       {


       }





        return markers;

    }

}
