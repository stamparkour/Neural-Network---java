package NeuralNetwork;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;


public class Layer {
	static double derOffset = 0.01;
	
	static Random random = new Random();
	public double[] bias;
	public double[][] weight;
	int ins, outs;
	
	public Layer(int ins, int outs) {
		this.ins = ins;
		this.outs = outs;
		bias = new double[outs];
		weight = new double[ins][outs];
	}
	
	public Layer(int ins, int outs, double range) {
		bias = new double[outs];
		weight = new double[ins][outs];
		this.ins = ins;
		this.outs = outs;
		
		for(int j = 0; j < outs; j++) {
			bias[j] = Random() * range;
			for(int i = 0; i < ins; i++) {
				weight[i][j] = Random() * range;
			}
		}
	}
	
	public Layer() {}
	
	public double[] Interprate(double[] input) {
		double[] o = new double[bias.length];
		for(int j = 0; j < outs; j++) {
			o[j] += bias[j];
			for(int i = 0; i < ins; i++) {
				o[j] += weight[i][j] * input[i];
			}
		}
		
		for(int j = 0; j < outs; j++) {
			o[j] = Curve(o[j]);
		}
		
		return o;
	}
	
	public void Change(double range)
    {
        for (int i = 0; i < outs; i++)
        {
            bias[i] += Random() * range;

            for (int j = 0; j < ins; j++)
            {
                weight[j][i] += Random() * range;
            }
        }
    }
	
	public double[] Change(double[] input, double[] delta, double rate)
    { 

        double[] d = new double[ins];
        for (int j = 0; j < outs; j++)
        {
            double v = -2 * delta[j] * DerWithBias(j, input);
            bias[j] -= v * rate;

            for (int i = 0; i < ins; i++)
            {
                double c = -2 * delta[j] * DerWithWeight(i, j, input);
                d[i] += c;
                weight[i][j] -= c * rate;
            }
        }

        for (int i = 0; i < ins; i++)
        {
            d[i] /= outs;
        }

        return d;
    }
	
	double Curve(double v) {
		return 2/(1+Math.pow(30,-v)) - 1;
	}
	
	double DerWithWeight(int indexA, int indexB, double[] input) {
		return (WithWeight(weight[indexA][indexB] + derOffset, indexA, indexB, input) - WithWeight(weight[indexA][indexB], indexA, indexB, input))/derOffset;
	}
	
	double WithWeight(double Weight, int indexA, int indexB, double[] input) {
		double n = bias[indexB];
		n += input[indexA] * Weight;
		for (int i = 0; i < ins; i++)
        {
            if(i != indexA) n += input[i] * weight[i][indexB];
        }
		 
		 n = Curve(n);
		 
		 return n;
	}
	
	double DerWithBias(int index, double[] input) {
		return (WithBias(bias[index] + derOffset, index, input) - WithBias(bias[index], index, input))/derOffset;
	}
	
	double WithBias(double bias, int index, double[] input) {
		double n = bias;

        for (int i = 0; i < ins; i++)
        {
            n += input[i] * weight[i][index];
        }

        n = Curve(n);

        return n;
	}
	
	static double Random() {
		return random.nextDouble() * 2 - 1;
	}
	
	 public Layer Clone()
     {

         double[] b = new double[outs];
         double[][] w = new double[ins][outs];

         for (int i = 0; i < outs; i++)
         {
             b[i] = bias[i];

             for (int j = 0; j < ins; j++)
             {
                 w[j][i] = weight[i][j];
             }
         }

         Layer layer = new Layer();
         layer.bias = b;
         layer.weight = w;

         return layer;
     }
	 
	 public byte[] Write()
     {
		 ByteBuffer buffer = ByteBuffer.allocate(8 + outs * 8 + ins * outs * 8);
		 buffer.order(ByteOrder.LITTLE_ENDIAN);
		 buffer.putInt(ins);
		 buffer.putInt(outs);
         for (int i = 0; i < outs; i++)
         {
        	 buffer.putDouble(bias[i]);
         }

         for (int i = 0; i < outs; i++)
         {
             for (int j = 0; j < ins; j++)
             {
            	 buffer.putDouble(weight[j][i]);
             }
         }

         return buffer.array();
     }

     public static Layer Read(ByteBuffer buffer)
     {
         int ins = buffer.getInt();
         int outs = buffer.getInt();

         Layer layer = new Layer(ins,outs);

         for (int i = 0; i < outs; i++)
         {
             layer.bias[i] = buffer.getDouble();
         }

         for (int i = 0; i < outs; i++)
         {
             for (int j = 0; j < ins; j++)
             {
                 layer.weight[j][i] =  buffer.getDouble();
             }
         }

         return layer;
     }
}
