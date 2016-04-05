


import edu.chalmers.moviezone_backend.Enjoyer;
import edu.chalmers.moviezone_backend.MovieZone;
import edu.chalmers.moviezone_backend.Rate;
import edu.chalmers.moviezone_backend.Review;
import java.util.List;
import java.util.Objects;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;


/**
 *
 * @author Rasti
 */


@Ignore
@RunWith(Arquillian.class)
public class TestDataBase {

    
    @Inject 
    MovieZone movie;

    @Inject
    UserTransaction utx;

    
    /* 
    *I have been trying for to many hours to get this method not to throw NullPointerException
    *Something is wrong with the setup and I can't figure out what it is and it 
    *irritates me so freaking much :(
    */
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "MovieZone.war")
                .addPackage(MovieZone.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }
    
    
    @Before  // Run before each password
    public void before() throws Exception {
        clearAll();
    }
    
    
    /*If we found the user through his ID and then made a double check 
        by comparing the username with the user we found with the user we earlier
        put in the database we have succeeded to put and find a user in a database.*/
    
    @Test
    public void putAndFindUserInDatabase() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        /*To put a enjoyer/user in a database you need 
        to use the create method*/
        movie.getEnjoyers().create(user); 
        Enjoyer foundUser = movie.getEnjoyers().find(user.getId());
        assertTrue(foundUser.getUserName().equals(user.getUserName()));
    }
    
    /*If we put some users in a database and then fetch them into a list
    with the findAll method and after that check if that lists size is equal to
    the amount of user we actually put in. Then we know that find all works as it should */ 
    @Test
    public void enjoyerPersistFindAll() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        user = new Enjoyer("itsar2", "password2");
        movie.getEnjoyers().create(user);
        user = new Enjoyer("itsar3", "password3");
        movie.getEnjoyers().create(user);
        List<Enjoyer> userList = movie.getEnjoyers().findAll();
        //Three users is created in to the database so the size of the list should be three
        assertTrue(userList.size() == 3);
        
    }
     /*
    1)Put some users in a database
    2)Fetch them to a list
    3)Check sizd of list to be the expected
    4)Delete a user
    5)Fetch all to list again
    6)See that that list is equal to other list -1
    */ 
    
    @Test
    public void enjoyerPersistDelete() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        user = new Enjoyer("itsar2", "password2");
        movie.getEnjoyers().create(user);
        user = new Enjoyer("itsar3", "password3");
        movie.getEnjoyers().create(user);
        List<Enjoyer> userList = movie.getEnjoyers().findAll();
        //Three users is created in to the database so the size of the list should be three
        assertTrue(userList.size() == 3);
        
        movie.getEnjoyers().delete(user.getId());
        //Three users is created one is deleted so size should be two
        List<Enjoyer> userListTwo = movie.getEnjoyers().findAll();
        assertTrue(userList.size() == (userListTwo.size())-1);
    }
        
  
    
    /*
    1) Create a user
    2) Change the firstname of the user through update
    3) Check that you can get the user thtough the new firstname
    4) Check that you no longer can get the user through the old firstname
    5) If password completes update works as should!
    */
    @Test
    public void enjoyerPersistUpdate() throws Exception {
        Enjoyer user = new Enjoyer("old", "password");
        movie.getEnjoyers().create(user);
        String oldName = user.getFname();
        List<Enjoyer> userList = movie.getEnjoyers().findAll();
        //Check if you actually can get the old first name first
        assertTrue(userList.get(0).getFname().equals(oldName));
        user.setFname("new");
        //doing the update users firstname should now be "new".
        movie.getEnjoyers().update(user);
        userList = movie.getEnjoyers().findAll();
        
        //Should not be able to get the user through the old name
        assertFalse(userList.get(0).getFname().equals("old"));
        assertTrue(userList.get(0).getFname().equals("new"));
        
    }
    
    /*
    *Check if you can get enjoyers through it's name. If many enyojers have the
    *same name then you should be able to fetch them all to a list through that name
    */
    @Test
    public void enjoyerPersistGetByName() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        user.setFname("same_name");
        movie.getEnjoyers().create(user);
        user = new Enjoyer("itsar2", "password2");
        user.setFname("same_name");
        movie.getEnjoyers().create(user);
        user = new Enjoyer("itsar3", "password3");
        user.setFname("another_name");
        movie.getEnjoyers().create(user);
        List<Enjoyer> userList = movie.getEnjoyers().getByName("same_name");
        assertTrue(userList.size() == 2);
    }
    
    /*
    *Check if you can get enjoyers through it's email. If many enyojers have the
    *same email then you should be able to fetch them all to a list through that email
    */
    @Test
    public void enjoyerPersistGetByEmail() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        user.setEmail("same@email.se");
        movie.getEnjoyers().create(user);
        user = new Enjoyer("itsar2", "password2");
        user.setEmail("same@email.se");
        movie.getEnjoyers().create(user);
        user = new Enjoyer("itsar3", "passwword3");
        user.setEmail("another@email.se");
        movie.getEnjoyers().create(user);
        List<Enjoyer> userList = movie.getEnjoyers().getByEmail("same@email.se");
        assertTrue(userList.size() == 2);
    }
    
    /*If we put some reviews by users in a database and then fetch them into a list
    with the findAll method and after that check if that lists size is equal to
    the amount of reviews we actually put in. Then we know that find all reviews works as it should
    */ 
    @Test
    public void reviewPersistFindAll() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        Review rate = new Review("theMovie", user.getId(),user.getUserName(), "I really love this movie!");
        movie.getReviews().create(rate);
        rate = new Review("theMovie2", user.getId(),user.getUserName(), "this one is actually a better one!");
        movie.getReviews().create(rate);
        List<Review> rsList = movie.getReviews().findAll();
        assertTrue(rsList.size() == 2 );
        // a check that it actually is the same as the last one I put in the database 
        assertTrue(rsList.get(1).getMovieId().equals(rate.getMovieId())); 
    }
    /*
    1)Put some reviews in a database
    2)Fetch them into a list
    3)Check size of list to be the expected
    4)Delete a review
    5)Fetch all to list again
    6)See that that list size is equal to other list size -1
    */ 
    @Test
    public void reviewPersistDelete() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        Review rate = new Review("theMovie", user.getId(),user.getUserName(), "I really love this movie!");
        movie.getReviews().create(rate);
        rate = new Review("theMovie2", user.getId(),user.getUserName(), "this one is actually a better one!");
        movie.getReviews().create(rate);
        List<Review> rsList = movie.getReviews().findAll();
        assertTrue(rsList.size() == 2 );
        movie.getReviews().delete(rate.getId());
        List<Review> rsListTwo = movie.getReviews().findAll();
        assertTrue(rsListTwo.size() == (rsList.size()-1));
    }
    
    /*
      Get a review through its text, edit that review, 
      get through the new text, try get through the old text
    */
    @Test
    public void reviewPersistUpdate() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        Review rate = new Review("theMovie", user.getId(),user.getUserName(), "Old text");
        movie.getReviews().create(rate);
        String oldText = rate.getReviewText();
        String newText = "New text";
        List<Review> rsList = movie.getReviews().findAll();
        assertTrue(rsList.get(0).getReviewText().equals(oldText));
        rate.setReviewText(newText);
        movie.getReviews().update(rate);
        rsList = movie.getReviews().findAll();
        assertFalse(rsList.get(0).getReviewText().equals("Old text"));
        assertTrue(rsList.get(0).getReviewText().equals(newText));
        
    }
    /*If we can find a movie review thtough it's id and 
    *we actually get that this review has the same movieId as the movie and userId of the author
    * then we can be quite certain that the find method for reviews work as it should.
    */
    @Test
    public void reviewPersistFind() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        Review rate = new Review("theMovie", user.getId(),user.getUserName(), "A good one");
        movie.getReviews().create(rate);
        Review theFoundReview = movie.getReviews().find(rate.getId());
        assertTrue(theFoundReview.getMovieId().equals(rate.getMovieId()));
        assertTrue(Objects.equals(theFoundReview.getUserId(), rate.getUserId()));
    }
    
    /*
    *Check if you can get reviews through a word in its text or complete text. If many reviews has the
    *same text then you should be able to fetch them all to a list through a word or text
    */
    @Test
    public void reviewPersistGetByText() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        Review rate = new Review("theMovie", user.getId(),user.getUserName(), "I will try to find a text through the word text" );
        movie.getReviews().create(rate);
        rate = new Review("anotherMovie", user.getId(),user.getUserName(), "as you can see text is in here to now");
        movie.getReviews().create(rate);
        rate = new Review("aThirdMovie", user.getId(),user.getUserName(), "I cant spell the word texst so texst is not in here ");
        movie.getReviews().create(rate);
        List<Review> rsList = movie.getReviews().getByText("text");
        //Since two of the reviews contains the word text
        assertTrue(rsList.size() == 2);
    }
    /*
    *Check if you can get reviews through a certain movie
    */
    @Test
    public void reviewPersistGetByMovie() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        Review rate = new Review("theMovie", user.getId(),user.getUserName(), "good movie");
        movie.getReviews().create(rate);
        user = new Enjoyer("itsar2", "password2");
        movie.getEnjoyers().create(user);
        rate = new Review("anotherMovie", user.getId(),user.getUserName(), "this one sucks");
        movie.getReviews().create(rate);
        rate = new Review("theMovie", user.getId(),user.getUserName(), "Wow love this!");
        movie.getReviews().create(rate);
        List<Review> rList = movie.getReviews().findAll();
        assertTrue(rList.size() == 3);
        //This will have the movieID of the last review which also happens to be the same as the first
        rList = movie.getReviews().getByMovie(rate.getMovieId()); 
        assertTrue(rList.size() == 2);
    }
    /*You should also be able to get all review from an specifik enjoyer*/
    @Test
    public void reviewPersistGetByEnjoyer() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        Review rate = new Review("theMovie", user.getId(),user.getUserName(), "Good");
        movie.getReviews().create(rate);
        user = new Enjoyer("itsar2", "password2");
        movie.getEnjoyers().create(user);
        //review from another user
        rate = new Review("anotherMovie", user.getId(),user.getUserName(), "Brad Pitt was not so good in this");
        movie.getReviews().create(rate);
        //review from same user as above
        rate = new Review("aThirdMovie", user.getId(),user.getUserName(), "Brad Pitt was awesome in this movie");
        movie.getReviews().create(rate);
        List<Review> rList = movie.getReviews().findAll();
        assertTrue(rList.size() == 3);
        rList = movie.getReviews().getByEnjoyer(user.getId());
        assertTrue(rList.size() == 2);
    }
    /*
    Check if it works to get all rates through the function find all
    */
    @Test
    public void ratePersistFindAll() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        Rate rate = new Rate("theMovie", user.getId(), 2);
        movie.getRatings().create(rate);
        rate = new Rate("anotherMovie", user.getId(), 4);
        movie.getRatings().create(rate);
        List<Rate> rList = movie.getRatings().findAll();
        assertTrue(rList.size() == 2);
    }
    /*
    1)Put some rates in a database
    2)Fetch them into a list
    3)Check size of list to be the expected
    4)Delete a rate
    5)Fetch all to list again
    6)See that that list size is equal to other list size -1
    */
    @Test
    public void ratePersistDelete() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        Rate rate = new Rate("theMovie", user.getId(), 2);
        movie.getRatings().create(rate);
        rate = new Rate("anotherMovie", user.getId(), 4);
        movie.getRatings().create(rate);
        List<Rate> rList = movie.getRatings().findAll();
        assertTrue(rList.size() == 2);
        movie.getRatings().delete(rate.getId());
        List<Rate> rListTwo = movie.getRatings().findAll();
        assertTrue(rListTwo.size() == (rList.size())-1);
    }
    /*Update rating with another value try to fetch it through old value 
    *that shouldnt work. Now try to fetch through the new value this should work.
    */
    @Test
    public void ratePersistUpdate() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        Rate rate = new Rate("theMovie", user.getId(), 2);
        movie.getRatings().create(rate);
        int oldRate = rate.getRating();
        List<Rate> rList = movie.getRatings().findAll();
        assertTrue(rList.size() == 1);
        assertTrue(rList.get(0).getRating() == oldRate);
        rate = new Rate(rate.getId(), rate.getMovieId(), rate.getUserId(), 4);
        movie.getRatings().update(rate);
        rList = movie.getRatings().findAll();
        assertTrue(rList.size() == 1);
        assertTrue(rList.get(0).getRating() == rate.getRating());
    }
    /*
    *Use find to get a Rating you put in the database make sure it is the same rating
    */
    @Test
    public void ratePersistFind() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        Rate rate = new Rate("theMovie", user.getId(), 2);
        movie.getRatings().create(rate);
        Rate foundRating = movie.getRatings().find(rate.getId());
        assertTrue(foundRating.getMovieId().equals(rate.getMovieId()));
        assertTrue(Objects.equals(foundRating.getUserId(), rate.getUserId()));
    }
    //I will try to find all movies which have been rated with a 5 in the database
    @Test
    public void ratePersistGetByRate() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        Rate rate = new Rate("theMovie", user.getId(), 5);
        movie.getRatings().create(rate);
        rate = new Rate("anotherMovie", user.getId(), 4);
        movie.getRatings().create(rate);
        rate = new Rate("aThirdMovie", user.getId(), 5);
        movie.getRatings().create(rate);
        rate = new Rate("theForthOne", user.getId(), 4);
        movie.getRatings().create(rate);
        List<Rate> rList = movie.getRatings().findAll();
        assertTrue(rList.size() == 4);
        rList = movie.getRatings().getByRate(5);
        assertTrue(rList.size() == 2);
    }
    /*
    *It's time to see if I can get rating through a certain moviein our database
    */
    @Test
    public void ratePersistGetByMovie() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        Rate rate = new Rate("theMovie", user.getId(), 2);
        movie.getRatings().create(rate);
        rate = new Rate("anotherMovie", user.getId(), 4);
        movie.getRatings().create(rate);
        user = new Enjoyer("itsar2", "password2");
        movie.getEnjoyers().create(user);
        rate = new Rate("theMovie", user.getId(), 2);
        movie.getRatings().create(rate);
        String theMovieToGet = rate.getMovieId();
        rate = new Rate("theForthOne", user.getId(), 4);
        movie.getRatings().create(rate);
        List<Rate> rList = movie.getRatings().findAll();
        assertTrue(rList.size() == 4);
        rList = movie.getRatings().getByMovie(theMovieToGet);
        assertTrue(rList.size() == 2);
    }
    /*
    *We also want to make sure that you can get all ratings from
    *a certain user in our database.
    */
    @Test
    public void ratePersistGetByEnjoyer() throws Exception {
        Enjoyer user = new Enjoyer("itsar", "password");
        movie.getEnjoyers().create(user);
        Rate rate = new Rate("theMovie", user.getId(), 2);
        movie.getRatings().create(rate);
        rate = new Rate("anotherMovie", user.getId(), 4);
        movie.getRatings().create(rate);
        user = new Enjoyer("itsar2", "password2");
        movie.getEnjoyers().create(user);
        rate = new Rate("theMovie", user.getId(), 2);
        movie.getRatings().create(rate);
        rate = new Rate("theForthOne", user.getId(), 4);
        movie.getRatings().create(rate);
        List<Rate> rList = movie.getRatings().findAll();
        assertTrue(rList.size() == 4);
        rList = movie.getRatings().getByEnjoyer(user.getId());
        assertTrue(rList.size() == 2);
    }

    @PersistenceContext(unitName = "jpa_moviezone_test_pu")
    
    @Produces
    @Default
    EntityManager em;

    // Order matters
    private void clearAll() throws Exception {
        utx.begin(); //start the transaction
        em.joinTransaction();
        em.createQuery("delete from Rate").executeUpdate();
        em.createQuery("delete from Review").executeUpdate();
        em.createQuery("delete from Enjoyer").executeUpdate();
        utx.commit(); //complete the transaction connected to the current Thread
    }

}
