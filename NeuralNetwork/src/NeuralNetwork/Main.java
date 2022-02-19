package NeuralNetwork;

public class Main {
	static double[][] ins = {
			{-1},
			{-.5},
			{0},
			{.5},
			{1},
		};
	
	static double[][] outs = {
			{1},
			{0.2},
			{0},
			{-0.7},
			{-0.1},
		};
	
	static Network network;
	
	public static void main(String[] args) {
		network = new Network(new int[] {1,3,3,1});
		double c = Cost(network);
		while(c > 0.01) {
			c = Change(100);
			Render(32);
			wait(100);
		}
	}
	
	public static void wait(int ms)
	{
	    try
	    {
	        Thread.sleep(ms);
	    }
	    catch(InterruptedException ex)
	    {
	        Thread.currentThread().interrupt();
	    }
	}
	
	static double Change(int itters) {
		for(int j = 0; j < itters; j++) {
			for(int i = 0;i < ins.length; i++) {
				network.Change(ins[i], outs[i], 0.02);
			}
		}
		
		return Cost(network);
	}
	
	static void Render(int width)
    {
        double half = width / 2d;

        double[] interp = new double[width];

        for (int x = 0; x < width; x++)
        {
            double X = x / half - 1;
            interp[x] = network.Interprate(new double[] { X })[0];
        }

        for (int y = 0; y < width; y++)
        {
            double Y = -(y / half - 1);
            double Y1 = -((y + 1) / half - 1);
            String s = "";
            for (int x = 0; x < width-1; x++)
            {
                //double X = x / half - 1;
                double X1 = (x + 1) / half - 1;

                boolean a = interp[x] > Y;
                boolean b = interp[x] > Y1;
                boolean c = interp[x+1] > Y;
                boolean d = interp[x+1] > Y1;

                if (!(a && b && c && d) && (a || b || c || d))
                {
                    s += "**";
                }
                else if (GetPointDistance(X1,Y) < 0.1)
                {
                    s += "[]";
                }
                else if (Y == 0)
                {
                    s += "--";
                }
                else if (x == (int)half)
                {
                    s += "||";
                }
                else
                {
                    s += "  ";
                }
            }

            System.out.println(s);
        }
        System.out.println(Cost(network));
        System.out.println(HexReader.bytesToHex(network.Write()));
    }
    
	static double GetPointDistance(double X, double Y)
    {
        double dist = Double.MAX_VALUE;
        for (int x = 0; x < ins.length; x++)
        {
            for (int y = 0; y < ins[x].length; y++)
            {
                double X1 = ins[x][ y]-X;
                double Y1 = outs[x][ y]-Y;
                double d = Math.sqrt(Math.pow(X1,2)+ Math.pow(Y1, 2));
                if (d < dist)
                {
                    dist = d;
                }
            }
        }

        return dist;
    }
	
    static double Cost(Network n)
    {
        double o = 0;
        for (int j = 0; j < ins.length; j++)
        {
            double[] d = n.Interprate(ins[j]);
            for (int i = 0; i < d.length; i++)
            {
                double v = d[i] - outs[j][i];
                o += Math.pow(v, 2);
            }
        }

        return o;
    }
}
