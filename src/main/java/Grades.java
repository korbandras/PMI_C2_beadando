public class Grades {
    private String Subject;
    private int Credit;
    private int Grade;

    public Grades(String Subject, int Credit, int Grade)
    {
        this.Subject = Subject;
        this.Credit = Credit;
        this.Grade = Grade;
    }
    public void setSubject(String subject)
    {
        this.Subject = subject;
    }
    public String getSubject()
    {
        return Subject;
    }
    public void setCredit(int credit)
    {
        this.Credit = credit;
    }
    public int getCredit()
    {
        return Credit;
    }
    public void setGrade(int grade)
    {
        this.Grade = grade;
    }
    public int getGrade()
    {
        return Grade;
    }
}