package quickcheck

import org.scalacheck.*
import Arbitrary.*
import Gen.*
import Prop.forAll

  abstract class QuickCheckHeap extends Properties("Heap") with IntHeap:
  lazy val genHeap: Gen[H] = oneOf(
    const(empty),
    for {
      a <- arbitrary[Int]
      h <- oneOf(const(empty), genHeap)
    } yield insert(a, h)
  )
  given Arbitrary[H] = Arbitrary(genHeap)

  property("gen1") = forAll { (h: H) =>
    val m = if isEmpty(h) then 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("min1") = forAll { (a: Int) =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  // If you insert any two elements into an empty heap,
  // finding the minimum of the resulting heap
  // should get the smallest of the two elements back.
  property("min2") = forAll { (a: Int, b: Int) =>
    val h = insert(a, empty)
    findMin(insert(b, h)) == (if a < b then a else b)
  }

  // If you insert an element into an empty heap,
  // then delete the minimum, the resulting heap should be empty.
  property("delete1") = forAll { (a: Int) =>
    val h = insert(a, empty)
    deleteMin(h) == empty
  }

  // Given any heap, you should get a sorted sequence of elements
  // when continually finding and deleting minima.
  // (Hint: recursion and helper functions are your friends.)
  property("delete2") = forAll { (h: H) =>
    def genSeq(h: H, seq: List[Int]) : List[Int] = {
      if isEmpty(h) then seq
      else findMin(h) :: genSeq(deleteMin(h), seq)
    }

    val list = genSeq(h, Nil)
    list == list.sorted
  }

  // Finding a minimum of the melding of any two heaps
  // should return a minimum of one or the other.
  property("meld") = forAll { (h1: H, h2: H) =>
    val m1 = findMin(h1)
    val m2 = findMin(h2)

    findMin(meld(h1, h2)) == (if m1 < m2 then m1 else m2)
  }

