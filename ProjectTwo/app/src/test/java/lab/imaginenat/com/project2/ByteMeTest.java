package lab.imaginenat.com.project2;

import org.junit.Test;

import lab.imaginenat.com.project2.models.Business;
import lab.imaginenat.com.project2.models.Place;

import static org.junit.Assert.assertEquals;
/**
 * Created by nat on 2/12/16.
 */
public class ByteMeTest {

    @Test
    public void testBusiness(){
        String testName="TestName";
        String testAddress="123 Test St.";
        String testState="NY";
        String testZip="10707";
        String type="no type";

        Business b = new Business(testName,testAddress,testState,testZip,type);
        assertEquals(testName, b.getName());
        assertEquals(testAddress,b.getAddress());
        assertEquals(testState,b.getState());
        assertEquals(testZip,b.getZip());

    }

    @Test
    public void testPlace(){
        //should throw a comma error
        String placeName="PlaceName";
        Place p = new Place(placeName);
        String address = "913 Broadway, New York, NY 10010, United States";
        p.setAddress(address);
        assertEquals("913 Broadway,",p.getBusinessAddress());
        assertEquals("NY",p.getBusinessState());

    }
}
