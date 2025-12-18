#include <iostream>
#include <string>
#include <vector>
using namespace std;

class Student {
public:
    int rollNumber;
    int age;
    string  name;
    string fatherName;
    string motherName;
    vector<string> subjects;
};

class StudentBuilder {
protected:
    Student student;

public:
    virtual ~StudentBuilder() = default;

    // Fluent setters
    StudentBuilder& setRollNumber(int rollNumber) {
        student.rollNumber = rollNumber;
        return *this;
    }

    StudentBuilder& setAge(int age) {
        student.age = age;
        return *this;
    }

    StudentBuilder& setName(const string& name) {
        student.name = name;
        return *this;
    }

    StudentBuilder& setFatherName(const string& fatherName) {
        student.fatherName = fatherName;
        return *this;
    }

    StudentBuilder& setMotherName(const string& motherName) {
        student.motherName = motherName;
        return *this;
    }

    StudentBuilder& setSubjects(const vector<string>& subjects) {
        student.subjects = subjects;
        return *this;
    }

    // Build method
    virtual Student build() = 0;
};

class EngineeringStudentBuilder : public StudentBuilder{
public:
    Student build() override
    {
        vector<string> v = {"DSA","ML","OS"};
        setSubjects(v);
        return student;
    }
};

class MBAStudentBuilder : public StudentBuilder{
public:
    Student build() override
    {
        vector<string> v = {"Operations","Business"};
        setSubjects(v);
        return student;
    }
};

class Director{
public:
    StudentBuilder& studentbuilder;

    Director(StudentBuilder& studentbuilder) : studentbuilder(studentbuilder) { }

    Student createStudent()
    {
        // <-- FIX: cannot compare to a type name. use dynamic_cast to detect concrete type.
        if (dynamic_cast<MBAStudentBuilder*>(&studentbuilder)) {
            return studentbuilder
                .setRollNumber(999)
                .setAge(25)
                .setName("MBA Student")
                .setFatherName("MBA Father")
                .setMotherName("MBA Mother")
                .setSubjects({"Operations","Business"})
                .build();
        }
        else if (dynamic_cast<EngineeringStudentBuilder*>(&studentbuilder)) {
            return studentbuilder
                .setRollNumber(100)
                .setAge(21)
                .setName("Engineering Student")
                .setFatherName("Eng Father")
                .setMotherName("Eng Mother")
                .setSubjects({"DSA","ML","OS"})
                .build();
        }
        // fallback: use builder as-is
        return studentbuilder.build();
    }
};

int main() {

    // demo Director usage
    EngineeringStudentBuilder eng;
    Director d(eng);
    Student student = d.createStudent();
    cout << "Director created: " << student.name << ", roll " << student.rollNumber << "\n";
    cout << "Roll: " << student.rollNumber << "\n";
    cout << "Age: " << student.age << "\n";
    cout << "Name: " << student.name << "\n";
    cout << "Father: " << student.fatherName << "\n";
    cout << "Mother: " << student.motherName << "\n";
    cout << "Subjects: ";

    for (const auto& s : student.subjects) {
        cout << s << " ";
    }

    cout << endl;
    MBAStudentBuilder mba;
    Director d1 (mba);
    student = d1.createStudent();
    cout << "Director created: " << student.name << ", roll " << student.rollNumber << "\n";
    cout << "Roll: " << student.rollNumber << "\n";
    cout << "Age: " << student.age << "\n";
    cout << "Name: " << student.name << "\n";
    cout << "Father: " << student.fatherName << "\n";
    cout << "Mother: " << student.motherName << "\n";
    cout << "Subjects: ";

    for (const auto& s : student.subjects) {
        cout << s << " ";
    }

    cout << endl;
    return 0;
}
