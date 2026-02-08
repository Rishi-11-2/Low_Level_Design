#include <iostream>
#include <vector>
#include <string>
#include <stack>
using namespace std;

// Originator with Memento inside
class ResumeEditor {
private:
    string name;
    string education;
    string experience;
    vector<string> skills;

public:
    void setName(const string& n) {
        name = n;
    }

    void setEducation(const string& e) {
        education = e;
    }

    void setExperience(const string& exp) {
        experience = exp;
    }

    void setSkills(const vector<string>& s) {
        skills = s;
    }

    void printResume() {
        cout << "x:----- Resume -----" << endl;
        cout << "Name: " << name << endl;
        cout << "Education: " << education << endl;
        cout << "Experience: " << experience << endl;
        cout << "Skills: ";
        for (const auto& skill : skills) cout << skill << " ";
        cout << endl;
        cout << "x:------------------" << endl;
    }

    // Inner Memento class
    class Memento {
    private:
        string name;
        string education;
        string experience;
        vector<string> skills;

    public:
        Memento(const string& n, const string& e, const string& exp, const vector<string>& s)
            : name(n), education(e), experience(exp), skills(s) {}

        friend class ResumeEditor;
    };

    // Save the current state as a Memento
    Memento save() {
        return Memento(name, education, experience, skills);
    }

    // Restore state from Memento
    void restore(const Memento& memento) {
        name = memento.name;
        education = memento.education;
        experience = memento.experience;
        skills = memento.skills;
    }
};

// Caretaker
class ResumeHistory {
private:
    stack<ResumeEditor::Memento> history;

public:
    void save(ResumeEditor& editor) {
        history.push(editor.save());
    }

    void undo(ResumeEditor& editor) {
        if (!history.empty()) {
            editor.restore(history.top());
            history.pop();
        }
    }
};

// Main driver
int main() {
    ResumeEditor editor;
    ResumeHistory history;

    editor.setName("Alice");
    editor.setEducation("B.Tech CSE");
    editor.setExperience("Fresher");
    editor.setSkills({"Java", "DSA"});
    history.save(editor);

    editor.setExperience("SDE Intern at TUF+");
    editor.setSkills({"Java", "DSA", "LLD", "Spring Boot"});
    history.save(editor);

    editor.printResume(); // Shows updated experience
    cout << endl;

    history.undo(editor);
    editor.printResume(); // Shows resume after one undo
    cout << endl;

    history.undo(editor);
    editor.printResume(); // Shows resume after second undo (initial state)

    return 0;
}
