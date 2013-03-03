public interface IGeometry
{
   public void add(Geometry child);    // add a child
   public Geometry getChild(int i);    // get the ith child
   public Matrix getMatrix();          // access the matrix
   public int getNumChildren();        // number of children
   public void remove(Geometry child); // remove a child
}
