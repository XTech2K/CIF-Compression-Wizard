//DisjointSets word for word from textboot if ignoring comments. Jake 3/27/2018

/**
 * Disjoint Sets class, using union by rank and path compression.
 */
public class DisjointSets
{
  
    public int[] s;
    public int size;
   
    /**
     * Constructs the disjoint set object.
     * @param numElements the initial number of disjoint sets.
     */
    public DisjointSets(int numElements)
    {
        this.s = new int[ numElements ];
        this.size = numElements;

        for( int i = 0; i < s.length; i++ )
            s[i] = -1;
    }
    
    /**
     * Union two disjoint sets using the height heuristic
     * @param root1 the root of set 1
     * @param root2 the root of set 2
     * @return the root of the resulting set
     * @throws IllegalArgumentException if root1 or root2 are not distinct
     */
    public int union( int root1, int root2 )
    {
        int finalRoot;
        assertIsRoot(root1);
        assertIsRoot(root2);
        if(root1==root2){
            throw new IllegalArgumentException("roots are equal");
        }
    
        if( s[ root2 ] < s[ root1 ]) { //root 2 is deeper
            finalRoot = root2;
            s[root1] = root2;
        } else {
            finalRoot = root1;
            if( s[ root1 ] == s[ root2 ])
                s[ root1 ]--; //update height if same
            s[ root2 ] = root1; //make root1 new root
        }

        size--;
        return finalRoot;

    }
  
    /**
     * Perform a find with path compression
     * @param x the element being searched for
     * @return the set containing x
     * @throws IllegalArgumentException if x is not valid
     */
    public int find( int x )
    {
        assertIsItem( x );
        if(s[ x ] < 0 )
            return x;
        else{
            return s[ x ] = find( s[ x ] );
        }
    }
    
    /**
     * Checks if the root is a valid root
     * @param root the root to check
     */
    void assertIsRoot( int root ){
        assertIsItem( root );
        if( s[ root ] >= 0)
            throw new IllegalArgumentException( );
    }
    /**
     * Checks if the item provided is a valid item
     * @param x the item to check
     */
    void assertIsItem( int x ){
        if( x < 0 || x >= s.length )
            throw new IllegalArgumentException( );
    }
}
