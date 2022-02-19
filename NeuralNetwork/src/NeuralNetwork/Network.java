package NeuralNetwork;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Network {
	public Layer[] layers;
	
	public Network(int[] lengths)
    {
        layers = new Layer[lengths.length - 1];

        for (int i = 0; i < layers.length; i++)
        {
            layers[i] = new Layer(lengths[i], lengths[i+1], 1);
        }
    }
	
	public Network() {}
	
	public void Change(double range)
    {
        for (int i = 0; i < layers.length; i++)
        {
            layers[i].Change(range);
        }
    }
	
	public void Change(double[] input, double[] want, double rate)
    {
        double[] a = want.clone();
        double[] b = Interprate(input.clone());
        for(int i = 0; i < want.length; i++) {
        	a[i] -= b[i];
        }
        for (int i = layers.length - 1; i >= 0; i--)
        {
            b = layers[i].Change(Interprate(input, i),a, rate);
            a = b;
        }
    }
	
	public double[] Interprate(double[] input)
    {
        return Interprate(input, layers.length);
    }
	
	double[] Interprate(double[] input, int maxLayer)
    {
        if (maxLayer == 0) return input;

        double[] a = input.clone();
        double[] b;

        for (int i = 0; i < maxLayer; i++)
        {
            b = layers[i].Interprate(a);

            a = b;
        }

        return a;
    }
	
	public Network Clone()
    {
        Layer[] l = new Layer[layers.length];

        for (int i = 0;i < l.length;i++)
        {
            l[i] = layers[i].Clone();
        }

        Network network = new Network();
        network.layers = l;

        return network;
    }
	
	public byte[] Write()
    {
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		int index = 4;
		buffer.putInt(layers.length);
        for (int i = 0; i < layers.length; i++)
        {
        	byte[] buf = layers[i].Write(); 
        	index += buf.length;
        	buffer.put(buf);
        }
        return Arrays.copyOfRange(buffer.array(),0,index);
    }

    public static Network Read(byte[] b)
    {
    	ByteBuffer buffer = ByteBuffer.wrap(b);
    	buffer.order(ByteOrder.LITTLE_ENDIAN);
        int ls = buffer.getInt();
        Network n = new Network();
        n.layers = new Layer[ls];

        for (int i = 0; i < ls; i++)
        {
            n.layers[i] = Layer.Read(buffer);
        }

        return n;
    }
}
