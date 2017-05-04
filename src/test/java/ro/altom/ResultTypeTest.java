package ro.altom;

import org.junit.Assert;
import org.junit.Test;
import ro.altom.results.BinaryResponse;
import ro.altom.results.PercentageResponse;
import ro.altom.results.PixelResponse;
import ro.altom.results.ResultException;

/**
 * Created by Altom on 06/10/16.
 */
public class ResultTypeTest {

    @Test
    public void binaryResult() {
        BinaryResponse response = new BinaryResponse();
        response.setResult(0, 100);
        Assert.assertTrue(response.getResult());
    }

    @Test
    public void binaryResultTenDiffPixels() {
        BinaryResponse response = new BinaryResponse();
        response.setResult(10, 100);
        Assert.assertFalse(response.getResult());
    }

    @Test
    public void percentageResult() throws Exception {
        PercentageResponse response = new PercentageResponse();
        response.setResult(25, 250);
        Assert.assertTrue(response.getResult() == 10);
    }

    @Test(expected = ResultException.class)
    public void percentageResultDivideByZero() throws Exception {
        PercentageResponse response = new PercentageResponse();
        response.setResult(0, 0);
    }

    @Test(expected = ResultException.class)
    public void percentageResultNegativeDenominator() throws Exception {
        PercentageResponse response = new PercentageResponse();
        response.setResult(0, -10);
    }

    @Test
    public void diffPixelResult() {
        PixelResponse response = new PixelResponse();
        response.setResult(5, 100);
        Assert.assertTrue(response.getResult() == 5);
    }

}
