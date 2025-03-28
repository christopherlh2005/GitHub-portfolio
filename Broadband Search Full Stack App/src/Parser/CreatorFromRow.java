package Parser;

import java.util.List;

/**
 * This interface allows all my "Creator" classes to operate polymorphically from the parser class.
 */
public interface CreatorFromRow<T> {
  T create(List<String> row) throws FactoryFailureException;
}
