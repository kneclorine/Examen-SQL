import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class Pizza extends Entity{

    private String name;
    private String photoURL;
    private final Set<Ingredient> ingredients = new HashSet<Ingredient>();
    private final Set<Comment> comments = new HashSet<Comment>();
    
    private final double profit = 1.20d;
    private double price;

    public Pizza(String name, String photoURL){
        this.name = name;
        this.photoURL = photoURL;
    }

    public String getName(){
        return this.name;
    }

    public String getPhotoURL(){
        return this.photoURL;
    }

    public double getPrice(){
        double finalPrice = 0d;
        Iterator<Ingredient> it = ingredients.iterator();

        while(it.hasNext()){
            finalPrice += it.next().getPrice();
        }
        this.price = Math.ceil((finalPrice * this.profit)*100)/100;
        return  this.price;
    }

    public Set<Ingredient> getIngredients(){
        return this.ingredients;
    }

    public Set<Comment> getComments(){
        return this.comments;
    }

    public void addIngredient(Ingredient ingredient){
        this.ingredients.add(ingredient);
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public void removeIngredient(Ingredient ingredient){
        this.ingredients.remove(ingredient);
    }

    public void removeComment(Comment comment){
        this.comments.remove(comment);
    }

    public void setName(String name){
        this.name = name;
    }    

    public void setPhotoURL(String photoURL){
        this.photoURL = photoURL;
    }
}
