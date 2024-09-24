public class Polynomial {
    
    public double[] coefficients;
    //field representing the coefficients

    public Polynomial() {
        this.coefficients = new double[]{0.0};
    }
    //no-argument constructor that sets 0

    public Polynomial(double[] input_coe){
        this.coefficients = input_coe;
    }
    //constructer

    public Polynomial add(Polynomial other){
        int result_length = Math.max(this.coefficients.length, other.coefficients.length);
        double[] result = new double[result_length];
        for (int i = 0; i < result_length; i++) {
            double this_coef = 0;
            double other_coef = 0;

            if( i < this.coefficients.length) {
                this_coef = this.coefficients[i];
            }
            if (i < other.coefficients.length) {
                other_coef = other.coefficients[i];
            }

            result[i] = this_coef + other_coef;
        }
        return new Polynomial(result);
    }

    public double evaluate(double x){
        double results = 0.0;
        for(int i = 0; i < this.coefficients.length; i++){
            results += this.coefficients[i] * Math.pow(x,i);
            // 2,5,3 -> 2 + 5x + 3x^2 -> 2*(x^0)+ 5*(x^1)+ 3(x^2)
        }
        return results;
    }

    public boolean hasRoot(double root_for_check){
       return this.evaluate(root_for_check) == 0;
    }

}
