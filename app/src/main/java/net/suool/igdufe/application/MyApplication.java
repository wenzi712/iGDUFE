package net.suool.igdufe.application;

import net.suool.igdufe.model.StudentInfo;
import net.suool.igdufe.service.CourseService;
import net.suool.igdufe.service.GradeService;
import net.suool.igdufe.service.LinkService;
import net.suool.igdufe.service.ReGradeService;
import net.suool.igdufe.service.StudentInfoService;

import org.litepal.LitePalApplication;

/**
 * Created by SuooL on 15/9/27.
 */
public class MyApplication extends LitePalApplication {
    private CourseService courseService;
    private LinkService linkService;
    private StudentInfoService studentInfoService;
    private GradeService gradeService;
    private ReGradeService reGradeService;
    @Override
    public void onCreate() {
        super.onCreate();
        courseService=new CourseService();
        linkService=new LinkService();
        studentInfoService = new StudentInfoService();
        gradeService = new GradeService();
        reGradeService = new ReGradeService();
    }
    public CourseService getCourseService() {
        return courseService;
    }
    public LinkService getLinkService() {
        return linkService;
    }

    public StudentInfoService getStudentInfoService(){
        return studentInfoService;
    }

    public GradeService getGradeService() {
        return gradeService;
    }

    public ReGradeService getReGradeService() {
        return reGradeService;
    }
}

