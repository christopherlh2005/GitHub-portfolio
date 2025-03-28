package Parser;

import java.util.List;

/** This is a class that creates new Student objects. */
public class StudentCreator implements CreatorFromRow<Student> {

  public StudentCreator() {}

  /**
   * @param row the specific row whose details you put in the argument
   * @return the new Student
   *     <p>This method creates a new Student object.
   */
  @Override
  public Student create(List<String> row) throws FactoryFailureException {
    if (row.size() != 10) {
      throw new FactoryFailureException("You don't have 10 parameters", row);
    }
    return new Student(
        row.get(0),
        row.get(1),
        row.get(2),
        row.get(3),
        row.get(4),
        row.get(5),
        row.get(6),
        row.get(7),
        row.get(8),
        row.get(9));
  }
}
