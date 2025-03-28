package Parser;

public class Student {

  public String ipedsRace;
  public String idYear;
  public String year;
  public String idUniversity;
  public String university;
  public String completions;
  public String slugUniversity;
  public String share;
  public String sex;
  public String idSex;

  /** This is a class that allows you to initialize new Student objects. */
  public Student(
      String ipedsRace,
      String idYear,
      String year,
      String idUniversity,
      String university,
      String completions,
      String slugUniversity,
      String share,
      String sex,
      String idSex) {

    this.ipedsRace = ipedsRace;
    this.idYear = idYear;
    this.year = year;
    this.idUniversity = idUniversity;
    this.university = university;
    this.completions = completions;
    this.slugUniversity = slugUniversity;
    this.share = share;
    this.sex = sex;
    this.idSex = idSex;
  }
}
