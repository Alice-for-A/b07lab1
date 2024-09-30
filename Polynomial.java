import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
public class Polynomial {
    
    public double [] coefficients; //coefficients array
    public int [] exponents; // exponents array

    //field representing the coefficients by two arrays

    public Polynomial() {
        this.coefficients = new double[]{0.0};
        this.exponents = new int[]{0};
    }
    //no-argument constructor that sets 0

    public Polynomial(double[] input_coe, int[] input_expo){
        this.coefficients = input_coe;
        this.exponents = input_expo;
    }
    //constructer

    /**
     * @param other
     * @return
     */
    public Polynomial add(Polynomial other){
        int index = 0;
        int max_length = this.coefficients.length + other.coefficients.length;
        //max length, as length of coef now is = length of exponents;
        double[] result_Coef = new double[max_length];
        int[] result_exp = new int[max_length];

        //loop each double in this polynomial
        for (int i = 0; i < this.coefficients.length; i++) {

            boolean found_equal_exp = false;
            double this_coef = 0;
            double other_coef = 0;

            for(int j = 0; j < other.coefficients.length; j++) {

                //loop in the other polynomial, add the coefficient together if there's a matched exp in other polynomial.
                if (this.exponents[i] == other.exponents[i]){
                    result_Coef[index] = this.coefficients[i] + other.coefficients[i];
                    result_exp[index] = this.exponents[i];
                    found_equal_exp = true;
                    break;
                }
            }
    
            //if no match found, add this term into result
            if (found_equal_exp != true){
                result_Coef[index] = this.coefficients[i];
                result_exp[index] = this.exponents[i];
            }

            index++; //added a new term to the result array, go next
        }

        // loop each double in the other polynomial
        for (int k = 0; k < other.coefficients.length; k++){
            boolean found_equal_exp = false;

            //skip the term if it has already been added
            for(int j = 0; j < this.coefficients.length; j++) {
                if (this.exponents[j] == other.exponents[j]){
                    found_equal_exp = true;
                    break;
                }
            }
            
            //add the term if it has not been addded before
            if (found_equal_exp != true){
                result_Coef[index] = other.coefficients[k];
                result_exp[index] = other.exponents[k];
                index++; //added a new term to the result array, go next
            }
        }
        return new Polynomial(result_Coef, result_exp);
    }

    public double evaluate(double x){
        double results = 0.0;

        for(int i = 0; i < this.coefficients.length; i++){
            results += this.coefficients[i] * this.exponents[i];
            // 2,5,3 -> 2 + 5x + 3x^2 -> 2*(x^0)+ 5*(x^1)+ 3(x^2)
        }
        return results;
    }

    public Polynomial multiply(Polynomial other){
        int index = 0;
        int max_length = this.coefficients.length * other.coefficients.length;
        //max length, as length of coef now is = length of exponents;
        double[] result_coef = new double[max_length];
        int[] result_exp = new int[max_length];

        //loop through the first polynomial and mutiply it with the other
        for(int i = 0; i < this.coefficients.length;i++){
            for (int j=0; j < other.coefficients.length; j++){
                result_coef[index] = this.coefficients[i] * other.coefficients[j];
                result_exp[index] = this.exponents[i] + other.exponents[j];
                //Mutiplying each term one by one
                index++;
            }
        }
        //loop each term in result  to combine the one with same exponents
        boolean exists = false;
        double[] true_coef = new double[max_length];
        int[] true_exp = new int[max_length];
        int count = 0;
        for(int j = 0; j < result_coef.length; j++){
    
            //loop each exponents with the "true" result to see if the same expo has already exists
            for(int k = j+1 ; k < result_coef.length; k++){
                if (true_exp[k] == result_exp[j]){
                 true_coef[k] += result_coef[j];
                 exists = true;
                 break;
                }
            }

            if (!exists) {
                true_coef[count] = result_coef[j];
                true_exp[count] = result_exp[j];
                count++;
            }
        }
        return new Polynomial(true_coef, true_exp);
    }

    public boolean hasRoot(double root_for_check){
       return this.evaluate(root_for_check) == 0;
    }

    /**
     * @param file
     * @throws FileNotFoundException
     */
    public Polynomial(File file) throws FileNotFoundException{

        Scanner scanner = new Scanner(file);
        String polynomialString = scanner.nextLine();
        scanner.close();

        String[] terms = polynomialString.split("(?=[+=])");
        int num_of_terms = terms.length;
        double[] coef_list = new double[num_of_terms];
        int[] exp_list = new int[num_of_terms];

        for (int i = 0; i< num_of_terms; i++) {
            double coef;
            int exp=0;
            String t = terms[i];

            if (t.contains("x")){
                String[] coef_exp = t.split("x");

                //if the string before "x" is "+" or not any
                if (coef_exp[0].equals("+")) {
                    coef = 1.0;
                }
                else if (coef_exp[0].equals("")){
                    coef = 1.0;
                }
                else if(coef_exp[0].equals("-")){
                    coef = -1.0;
                }
                else{
                    coef = Double.parseDouble(coef_exp[0]);
                }

                if((coef_exp.length) > 1) {
                    exp = Integer.parseInt(coef_exp[1]);
                }
                else if(coef_exp.length == 1) {
                    exp = 1;
                } 
            }
            else{
                coef = Double.parseDouble(t);
                exp = 0;
            }
            coef_list[i] = coef;
            exp_list[i] = exp;
        }
        this.coefficients = coef_list;
        this.exponents = exp_list;
    }

    public void saveToFile(String filename) throws IOException{
        FileWriter fw= new FileWriter(filename);
        PrintWriter pw = new PrintWriter(fw);

        //loop to write from both the coef and degree
        for(int i=0; i< coefficients.length; i++) {
            //normal coef
            if(coefficients[i] != 1 || exponents[i]== 0){
                pw.print(coefficients[i]);
            }
            //+coef
            if (coefficients[i] >= 0 && i != 0){
                pw.print("+");
            }

            //EXP, must >1 to show a number here
            if(exponents[i] != 0) {
                pw.print("x");
                if(exponents[i] != 1){
                    pw.print(exponents[i]);
                }
            }
        }
        pw.close();
    }
}
