package com.drive.core.models;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class StudentModel {

    @ChildResource(name = "studentItems")
    private Resource studentItems;

    public List<Student> getStudents() {

        if (studentItems == null) {
            return Collections.emptyList();
        }

        List<Student> students = new ArrayList<>();

        for (Resource studentResource : studentItems.getChildren()) {
            students.add(new Student(studentResource));
        }

        return students;
    }

    public static class Student {

        private final Resource resource;

        public Student(Resource resource) {
            this.resource = resource;
        }

        public String getStudentName() {
            return resource.getValueMap()
                    .get("studentName", String.class);
        }

        public String getSpecialization() {
            return resource.getValueMap()
                    .get("specialization", String.class);
        }

        public List<Subject> getSubjects() {

            Resource subjectsResource =
                    resource.getChild("subjects");

            if (subjectsResource == null) {
                return Collections.emptyList();
            }

            List<Subject> subjects = new ArrayList<>();

            for (Resource subjectResource :
                    subjectsResource.getChildren()) {

                subjects.add(new Subject(subjectResource));
            }

            return subjects;
        }
    }

    public static class Subject {

        private final Resource resource;

        public Subject(Resource resource) {
            this.resource = resource;
        }

        public String getSubjectName() {
            return resource.getValueMap()
                    .get("subjectName", String.class);
        }

        public String getSubjectDescription() {
            return resource.getValueMap()
                    .get("subjectDescription", String.class);
        }
    }
}