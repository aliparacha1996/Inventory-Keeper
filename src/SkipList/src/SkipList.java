package SkipList.src;// Author: Ali Paracha
// This is a class that implements the SkipList data structure, created by William Pugh in 1989.

import java.util.*;

public class SkipList<T extends Comparable>  {

    //   Class that defines the nodes to be used inside a skipList.
//   Each Node contains an arraylist of references to next nodes, where the index in the arrayList
//   describes the lane we are in.
    private class skipListNode<T extends Comparable> {
       private ArrayList<skipListNode<T>> refs;
       private T element;

        private skipListNode(T element) {
            this.refs = new ArrayList<>();
            this.element = element;
//            this.refs.add(null);
        }

    }

    private skipListNode<T> head;
    private int lanes; // total number of lanes
    private int count; // total unique elements in the skipList
    private Random coin = new Random();

//  Method to flipCoin thats expected to return true 50% of the time
    private boolean flipCoin() {
        return coin.nextInt(2) == 1;
    }


//  Default construct, create dummy head node and list with one lane.
    public SkipList() {
        head = new skipListNode<>(null);
        head.refs.add(null);
        lanes = 1;
        count = 0;
    }

//  Constructor that creates new dummy head of list with element firstElement
    public SkipList(T firstElement){
        head = new skipListNode<>(null);
        lanes = 1;
        count = 0;
        head.refs.add(null);
        insBetNodes(head, new skipListNode<>(firstElement), 0);
    }

//  Count number of elements in a specific lane
    public int laneCount(int lane) {
        int count = 0;
        skipListNode<T> curr = head;
//      traverse list on that lane
        while (curr.refs.get(lane) != null) {
            curr = curr.refs.get(lane);
            count++;
        }
        return count;
    }

//    get the total number of unique elements in the list
    public int getCount() {
        return count;
    }

//    get total number of elements in the list (elements in each lane added up)
    public int getTotalCount() {
        int sum = 0;
        skipListNode<T> curr = head;
        while(curr.refs.get(0) != null) {
            sum += curr.refs.get(0).refs.size();
            curr = curr.refs.get(0);
        }
        return sum;
    }

//    private method to help in insertion. Inserts the node toInsert in between prev and the next element
//    to which precious points in lane lane.
    private void insBetNodes(skipListNode<T> prev, skipListNode<T> toInsert, int lane) {
        toInsert.refs.add(prev.refs.get(lane));
        prev.refs.remove(lane);
        prev.refs.add(lane, toInsert);
    }

    //  Insert Method to insert an element into the skipList. Returns true if new element added to the list, false
    //  otherwise. (This means no duplicates allowed)
    public boolean insert(T element) {
//        Stack to keep track of path we came from
        Stack<skipListNode<T>> pathStack = new Stack<>();
        int currLane = lanes - 1;
        skipListNode<T> curr = head;

        while(currLane >= 0) {
//          Traverse current lane until the next element you look at is null or greater than the element we need to insert.
//          If it is then, add the current node to the stack, as this is the node (in this lane) after which we will need to insert
//          the element we are trying to insert. Followed by decreasing the lane number as now we will look where we
//          need to insert the element in the next lane.
            if(curr.refs.get(currLane) == null || curr.refs.get(currLane).element.compareTo(element) >= 0) {
//               If the element you stopped at is the same as the element that was passed in, then return false as we do not
//               allow duplicates
                if (curr.refs.get(currLane) != null && curr.refs.get(currLane).element.compareTo(element) == 0)
                    return false;

                pathStack.push(curr);
                currLane -= 1;
            }
//          Else move forward on to next node
            else curr = curr.refs.get(currLane);
        }

//        Else create new node to insert. Add to the base lane/list and remove the previous pointer in that lane
//        from the stack
        skipListNode<T> toInsert = new skipListNode<>(element);
        insBetNodes(pathStack.pop(), toInsert, 0);
        currLane = 1; // add to other lanes if coin flip deems it to be so

//          Repeat until path stack is empty or until our flip is false/negative (as this decides whether we add/elevate
//          the element to the next lane.
        while(!pathStack.isEmpty()) {
            if(flipCoin()) {
//              Add to current lane and remove from stack the prev pointer in that lane
                insBetNodes(pathStack.pop(), toInsert, currLane);
                currLane++;
            }
            else break;
        }

//      if you elevated the element to be inserted all the way to the top, decide if it needs to be elevated further
//      (hence creating new lanes) via coin flip.
        while(currLane == lanes && flipCoin()) {
          head.refs.add(null); // add new lane
          insBetNodes(head, toInsert, currLane);
          lanes += 1; // increment number of lanes
          currLane += 1;
        }
        count++; // increment count as an element was added
        return true;

    }

    //  Search method to search for element passed in. Returns element if element is in the list, null otherwise.
    public T search(T element) {
        int currLane = lanes - 1;
        skipListNode<T> curr = head;
        while(currLane >= 0) {
            if(curr.refs.get(currLane) == null || curr.refs.get(currLane).element.compareTo(element) > 0)
                currLane -= 1;
            else if(curr.refs.get(currLane).element.compareTo(element) == 0) return curr.refs.get(currLane).element;
            else curr = curr.refs.get(currLane);
        }
        return null;
    }

//  Remove the node that previous has a reference to in lane lane. If the deletion of this node leads to the head ref
//  pointing to only null in lane lane, collapse that lane. returns true if lane collapsed, false otherwise.
    private boolean removeAndCollapse(skipListNode<T> prev, int lane) {
//     Add to the lane the element that the element prev points to points to.
        prev.refs.add(lane, prev.refs.get(lane).refs.get(lane));
//      Remove the element that previous originally pointed to
        prev.refs.remove(lane + 1);
//      if removal leads to head having only null element in lane, collapse that lane (except if lane is base lane)
        if(head.refs.get(lane) == null && lane != 0) {
            head.refs.remove(lane);
            lanes -= 1;
            return true;
        }
        else return false;
    }
    //  Method to delete the element passed in from the skipList. Returns true if element existed and was
    //  deleted from the list, false otherwise.
    public boolean delete(T element) {
        // Stack to keep track of all previous pointers from which we need to delete
       Stack<skipListNode<T>> prevPointers = new Stack<>();
       skipListNode<T> curr = head;
       int currLane = lanes - 1;

       while(currLane >= 0) {
//         if next element is null or greater than or equal to than element we want to delete switch lanes.
           if(curr.refs.get(currLane) == null || curr.refs.get(currLane).element.compareTo(element) >= 0) {
//             if next element is equal to the element was want to delete, add current to stack because this is the node
//             whose references we will change in the appropriate lanes
               if (curr.refs.get(currLane) != null && curr.refs.get(currLane).element.compareTo(element) == 0)
                   prevPointers.push(curr);
               currLane -=1;
           }
           else curr = curr.refs.get(currLane);
       }
//     Element to delete not present in list
       if(prevPointers.isEmpty()) return false;

       currLane = 0;
//      Remove element from each lane starting from 0.
        while(!prevPointers.isEmpty()) {
            if(!removeAndCollapse(prevPointers.pop(), currLane))
                currLane += 1;
        }
//      Decrement count
        count--;
        return true;
    }

//  Iterator for list, iterates  through all the elements within the list (not fail fast)
    public Iterator<T> iterator() {
        ArrayList<T> iter = new ArrayList<>();
        skipListNode<T> curr = head;
        while (curr.refs.get(0) != null) {
            iter.add(curr.element);
            curr = curr.refs.get(0);
        }
        return iter.iterator();
    }

//  Clear the skiplist of any elements
    public void clear() {
        head = new skipListNode<>(null);
        head.refs.add(null);
        lanes = 1;
        count = 0;
        System.gc(); // Hint to compiler to perform garbage collection
    }

//  String representation of skip list.
//  Shows each lane number followed by the elements within that lane.
    public String toString() {
        StringBuilder lane =  new StringBuilder();
        for(int i = 0; i < lanes; i++) {
            lane.append("Lane: " + (i + 1) + " ");
            skipListNode<T> curr = head;
            while(curr.refs.get(i) != null) {
                curr = curr.refs.get(i);
                lane.append(curr.element + ", ");
            }
            lane.append(null + "\n");
        }
        return lane.toString();
    }

    public boolean isEmpty() {
        return count  == 0;
    }

//   Get the Total number of lanes in skipList
    public int getLanes() {
        return lanes;
    }

    public boolean contains(T element) {
        return search(element) != null;
    }

    public int getSize() {
        return count;
    }

    public int totalUniqueElements() {
        return getTotalCount();
    }
}
