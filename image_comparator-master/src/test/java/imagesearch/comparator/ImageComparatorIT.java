package imagesearch.comparator;

import imagesearch.image.CustomImageType;
import imagesearch.testutils.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class ImageComparatorIT {

    @Spy
    private ComparatorCache cache = spy(new ComparatorCache());

    @Spy
    private ImageComparator imageComparator = spy(new ImageComparator(cache, 4, 5, 10));

    @Test
    public void testCompareDoNotMatch(){
        throw new AssertionError();
    }

    @Test
    public void testCompareMatch(){
        CustomImageType img1 = TestUtil.readCustomImageType("img1.jpg");
        CustomImageType img2 = TestUtil.readCustomImageType("img1.jpg");
        imageComparator.compare(img1, img2);
        System.out.println();
    }
}
