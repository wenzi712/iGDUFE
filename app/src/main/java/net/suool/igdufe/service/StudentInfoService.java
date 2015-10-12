package net.suool.igdufe.service;

import android.util.Log;

import net.suool.igdufe.model.StudentInfo;
import net.suool.igdufe.util.SharedPreferenceUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by SuooL on 15/10/6.
 */
public class StudentInfoService {

    public boolean save(StudentInfo linknode){
        return linknode.save();
    }
    /**
     * 查询所有链接
     *
     * @return
     */
    public List<StudentInfo> findAll() {
        return DataSupport.findAll(StudentInfo.class);
    }

    public void parseBasicInfo(byte[] result){
    }

}
