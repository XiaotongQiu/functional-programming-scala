package funsets

/**
 * This class is a test suite for the methods in object FunSets.
 *
 * To run this test suite, start "sbt" then run the "test" command.
 */
class FunSetSuite extends munit.FunSuite:

  import FunSets.*

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets:
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(1)

  /**
   * This test is currently disabled (by using .ignore) because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", remove the
   * .ignore annotation.
   */

    test("singleton set one contains one") {
    
    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets:
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
      assert(!contains(s1, 2), "Singleton 2")
  }

  test("union contains all elements of each set") {
    new TestSets:
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
  }

  test("intersect contains common elements of each set") {
    new TestSets:
      assert(!contains(intersect(s1, s2), 1), "Intersect 1&2")
      assert(contains(intersect(s1, s4), 1), "Intersect 1&4")
  }

  test("diff contains all elements of `s` that are not in `t`") {
    new TestSets:
      assert(contains(diff(s1, s2), 1), "diff 1&2")
      assert(!contains(diff(s1, s4), 1), "diff 1&4")
      assert(!contains(diff(s1, s2), 2), "diff 1&2 with 2")
  }

  test("filter s with some condition p") {
    new TestSets:
      assert(!contains(filter(s1, x => x%2==0), 1), "filter all even elements")
      assert(contains(filter(s1, x => x%2==1), 1), "filter all odd elements")
  }

  test("for all") {
    new TestSets:
      val s = union(union(s1, s2), s3)
      assert(forall(s, x => x>0), "all positive")
      assert(!forall(s, x => x%2==0), "all even")
  }

  test("exist") {
    new TestSets:
      val s = union(union(s1, s2), s3)
      assert(exists(s, x => x > 0), "exists positive")
      assert(exists(s, x => x % 2 == 0), "exists even")
  }

  test("map") {
    new TestSets:
      val s = union(union(s1, s2), s3)
      assert(contains(map(s, x => x*2), 4), "map and get 4")
      assert(!contains(map(s, x => x*2), 5), "map won't get 5")
  }
  import scala.concurrent.duration.*
  override val munitTimeout = 10.seconds
