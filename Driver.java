import java.util.ListIterator;

public class Driver {
	public static void main(String[] args){
		ArrayList<Integer> L = new ArrayList<Integer>();
		for(int i=0; i<5; i++) 
			L.add(i, i+10);
		System.out.println(L);
		ListIterator<Integer> it=L.myListIterator();
		System.out.println("L begin to end");
		while(it.hasNext()){
			System.out.print(it.next()+" ");
		}
		System.out.println("L end to begin");
		//System.out.println(it.remove());
		while(it.hasPrevious()){
			System.out.print(it.previous()+" ");
		}
		System.out.println();
		it.set(111);
		it.next();
		it.set(123);
		System.out.println("L after set begin to end");
		while(it.hasNext()){
			System.out.print(it.next()+" ");
		}
		System.out.println();
		
		ArrayList<Integer> M = new ArrayList<Integer>();
		M.add(0, 1);
		M.add(1, 2);
		M.add(2, 3);
		M.add(3, 4);
		M.add(4, 5);
		System.out.println("M:"+M);
		ArrayList<Integer> P = new ArrayList<Integer>();
		P.add(0, 2);
		P.add(1, 3);
		System.out.println("P:"+P);
		System.out.println("After remove position: "+removePositions(M,P));
	}
	
	public static ArrayList<Integer> removePositions(ArrayList<Integer> L,ArrayList<Integer> P){
		int count=0;
		ListIterator<Integer> it2=P.myListIterator();
		while(it2.hasNext()){
			int temp=it2.next()-count;
			ListIterator<Integer> it=L.myListIterator(temp);
			it.next();
			it.remove();
			count++;
		}
		return L;
	}
}