package org.roaringbitmap;

import org.junit.Assert;
import org.junit.Test;
import org.roaringbitmap.buffer.ImmutableRoaringBitmap;
import org.roaringbitmap.buffer.MutableRoaringBitmap;

public class TestImmutableRoaringBitmapOrNot {
    @Test
    public void orNot1() {
        final MutableRoaringBitmap rb = new MutableRoaringBitmap();
        final MutableRoaringBitmap rb2 = new MutableRoaringBitmap();

        rb.add(2);
        rb.add(1);
        rb.add(1 << 16); // 65 536
        rb.add(2 << 16); //131 072
        rb.add(3 << 16); //196 608

        rb2.add(1 << 16);// 65 536
        rb2.add(3 << 16);//196 608

        MutableRoaringBitmap answer = ImmutableRoaringBitmap.orNot(rb, rb2, (4 << 16) - 1);

        Assert.assertEquals((4 << 16) - 1, answer.getCardinality());

        final IntIterator iterator = answer.getIntIterator();

        for (int i = 0; i < (4 << 16) - 1; ++i) {
            Assert.assertTrue("Error on iteration " + i, iterator.hasNext());
            Assert.assertEquals(i, iterator.next());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void orNot2() {
        final MutableRoaringBitmap rb = new MutableRoaringBitmap();
        final MutableRoaringBitmap rb2 = new MutableRoaringBitmap();

        rb.add(0);
        rb.add(1 << 16); // 65 536
        rb.add(3 << 16); //196 608

        rb2.add((4 << 16) - 1); //262 143

        MutableRoaringBitmap answer = ImmutableRoaringBitmap.orNot(rb, rb2, 4 << 16);

        Assert.assertEquals((4 << 16) - 1, answer.getCardinality());

        final IntIterator iterator = answer.getIntIterator();

        for (int i = 0; i < (4 << 16) - 1; ++i) {
            Assert.assertTrue("Error on iteration " + i, iterator.hasNext());
            Assert.assertEquals(i, iterator.next());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void orNot3() {
        final MutableRoaringBitmap rb = new MutableRoaringBitmap();
        rb.add(2 << 16);

        final MutableRoaringBitmap rb2 = new MutableRoaringBitmap();
        rb2.add(1 << 14); //16 384
        rb2.add(3 << 16); //196 608

        MutableRoaringBitmap answer = ImmutableRoaringBitmap.orNot(rb, rb2, (5 << 16));
        Assert.assertEquals((5 << 16) - 2, answer.getCardinality());

        final IntIterator iterator = answer.getIntIterator();
        for (int i = 0; i < (5 << 16); ++i) {
            if ((i != (1 << 14)) && (i != (3 << 16))) {
                Assert.assertTrue("Error on iteration " + i, iterator.hasNext());
                Assert.assertEquals("Error on iteration " + i, i, iterator.next());
            }
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void orNot4() {
        final MutableRoaringBitmap rb = new MutableRoaringBitmap();
        rb.add(1);

        final MutableRoaringBitmap rb2 = new MutableRoaringBitmap();
        rb2.add(3 << 16); //196 608

        MutableRoaringBitmap answer = ImmutableRoaringBitmap.orNot(rb, rb2, (2 << 16) + (2 << 14)); //131 072 + 32 768 = 163 840
        Assert.assertEquals((2 << 16) + (2 << 14), answer.getCardinality());

        final IntIterator iterator = answer.getIntIterator();
        for (int i = 0; i < (2 << 16) + (2 << 14); ++i) {
            Assert.assertTrue("Error on iteration " + i, iterator.hasNext());
            Assert.assertEquals("Error on iteration " + i, i, iterator.next());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void orNot5() {
        final MutableRoaringBitmap rb = new MutableRoaringBitmap();

        rb.add(1);
        rb.add(1 << 16); // 65 536
        rb.add(2 << 16); //131 072
        rb.add(3 << 16); //196 608

        final MutableRoaringBitmap rb2 = new MutableRoaringBitmap();

        MutableRoaringBitmap answer = ImmutableRoaringBitmap.orNot(rb, rb2, (5 << 16));
        Assert.assertEquals((5 << 16), answer.getCardinality());

        final IntIterator iterator = answer.getIntIterator();
        for (int i = 0; i < (5 << 16); ++i) {
            Assert.assertTrue("Error on iteration " + i, iterator.hasNext());
            Assert.assertEquals("Error on iteration " + i, i, iterator.next());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void orNot6() {
        final MutableRoaringBitmap rb = new MutableRoaringBitmap();

        rb.add(1);
        rb.add((1 << 16) - 1); // 65 535
        rb.add(1 << 16); // 65 536
        rb.add(2 << 16); //131 072
        rb.add(3 << 16); //196 608

        final MutableRoaringBitmap rb2 = new MutableRoaringBitmap();

        MutableRoaringBitmap answer = ImmutableRoaringBitmap.orNot(rb, rb2, (1 << 14));

        // {[0, 2^14], 65 535, 65 536, 131 072, 196 608}
        Assert.assertEquals((1 << 14), answer.getCardinality());

        final IntIterator iterator = answer.getIntIterator();
        for (int i = 0; i < (1 << 14); ++i) {
            Assert.assertTrue("Error on iteration " + i, iterator.hasNext());
            Assert.assertEquals("Error on iteration " + i, i, iterator.next());
        }

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void orNot7() {
        final MutableRoaringBitmap rb = new MutableRoaringBitmap();

        rb.add(1 << 16); // 65 536
        rb.add(2 << 16); //131 072
        rb.add(3 << 16); //196 608

        final MutableRoaringBitmap rb2 = new MutableRoaringBitmap();

        MutableRoaringBitmap answer = ImmutableRoaringBitmap.orNot(rb, rb2, (1 << 14));

        // {[0, 2^14], 65 536, 131 072, 196 608}
        Assert.assertEquals((1 << 14), answer.getCardinality());

        final IntIterator iterator = answer.getIntIterator();
        for (int i = 0; i < (1 << 14); ++i) {
            Assert.assertTrue("Error on iteration " + i, iterator.hasNext());
            Assert.assertEquals("Error on iteration " + i, i, iterator.next());
        }

        Assert.assertFalse(iterator.hasNext());
    }



    @Test
    public void orNot9() {
        final MutableRoaringBitmap rb1 = new MutableRoaringBitmap();

        rb1.add(1 << 16); // 65 536
        rb1.add(2 << 16); //131 072
        rb1.add(3 << 16); //196 608


        {
            final MutableRoaringBitmap rb2 = new MutableRoaringBitmap();
            final MutableRoaringBitmap answer1 = MutableRoaringBitmap.orNot(rb1, rb2, (1 << 14));

            // {[0, 2^14]}
            Assert.assertEquals(1 << 14, answer1.getCardinality());

            final IntIterator iterator1 = answer1.getIntIterator();
            for (int i = 0; i < (1 << 14); ++i) {
                Assert.assertTrue("Error on iteration " + i, iterator1.hasNext());
                Assert.assertEquals("Error on iteration " + i, i, iterator1.next());
            }
            Assert.assertFalse(iterator1.hasNext());
        }

        {
            final MutableRoaringBitmap rb2 = new MutableRoaringBitmap();
            final MutableRoaringBitmap answer = MutableRoaringBitmap.orNot(rb1, rb2, (2 << 16));

            // {[0, 2^16]}
            Assert.assertEquals(2 << 16, answer.getCardinality());

            final IntIterator iterator = answer.getIntIterator();
            for (int i = 0; i < (2 << 16); ++i) {
                Assert.assertTrue("Error on iteration " + i, iterator.hasNext());
                Assert.assertEquals("Error on iteration " + i, i, iterator.next());
            }
            Assert.assertFalse("Number of elements " + (2 << 16) , iterator.hasNext());
        }


        {
            final MutableRoaringBitmap rb2 = new MutableRoaringBitmap();
            rb2.add((1 << 16) + (1 << 13));
            rb2.add((1 << 16) + (1 << 14));
            rb2.add((1 << 16) + (1 << 15));
            final MutableRoaringBitmap answer = MutableRoaringBitmap.orNot(rb1, rb2, (2 << 16));

            // {[0, 2^16]}
            Assert.assertEquals((2 << 16) - 3, answer.getCardinality());

            final IntIterator iterator = answer.getIntIterator();
            for (int i = 0; i < (2 << 16); ++i) {
                if ((i != (1 << 16) + (1 << 13)) && (i != (1 << 16) + (1 << 14)) && (i != (1 << 16) + (1 << 15))) {
                    Assert.assertTrue("Error on iteration " + i, iterator.hasNext());
                    Assert.assertEquals("Error on iteration " + i, i, iterator.next());
                }
            }
            Assert.assertFalse("Number of elements " + (2 << 16) , iterator.hasNext());
        }

        {
            final MutableRoaringBitmap rb2 = new MutableRoaringBitmap();
            rb2.add(1 << 16);
            rb2.add(3 << 16);
            rb2.add(4 << 16);

            final MutableRoaringBitmap answer = MutableRoaringBitmap.orNot(rb1, rb2, (5 << 16));

            // {[0, 2^16]}
            Assert.assertEquals((5 << 16) - 1, answer.getCardinality());

            final IntIterator iterator = answer.getIntIterator();
            for (int i = 0; i < (5 << 16); ++i) {
                if (i != (4 << 16)) {
                    Assert.assertTrue("Error on iteration " + i, iterator.hasNext());
                    Assert.assertEquals("Error on iteration " + i, i, iterator.next());
                }
            }
            Assert.assertFalse("Number of elements " + (2 << 16) , iterator.hasNext());
        }

        {
            final MutableRoaringBitmap rb2 = new MutableRoaringBitmap();
            rb2.add(1 << 16);
            rb2.add(3 << 16);
            rb2.add(4 << 16);

            final MutableRoaringBitmap answer = MutableRoaringBitmap.orNot(rb2, rb1, (5 << 16));

            // {[0, 2^16]}
            Assert.assertEquals((5 << 16) - 1, answer.getCardinality());

            final IntIterator iterator = answer.getIntIterator();
            for (int i = 0; i < (5 << 16); ++i) {
                if (i != (2 << 16)) {
                    Assert.assertTrue("Error on iteration " + i, iterator.hasNext());
                    Assert.assertEquals("Error on iteration " + i, i, iterator.next());
                }
            }
            Assert.assertFalse("Number of elements " + (2 << 16) , iterator.hasNext());
        }
    }

    @Test
    public void orNot10() {
        final MutableRoaringBitmap rb = new MutableRoaringBitmap();
        final MutableRoaringBitmap rb2 = new MutableRoaringBitmap();

        rb.add(5);
        rb2.add(10);

        MutableRoaringBitmap answer = ImmutableRoaringBitmap.orNot(rb, rb2, 6);

        Assert.assertEquals(5, answer.last());
    }

    @Test
    public void orNot11() {
        final MutableRoaringBitmap rb = new MutableRoaringBitmap();
        final MutableRoaringBitmap rb2 = new MutableRoaringBitmap();

        rb.add((int) (65535L * 65536L + 65523));
        rb2.add((int) (65493L * 65536L + 65520));

        MutableRoaringBitmap rb3 = MutableRoaringBitmap.orNot(rb, rb2, 65535L * 65536L + 65524);

        Assert.assertEquals((int)(65535L * 65536L + 65523), rb3.last());
    }


}
