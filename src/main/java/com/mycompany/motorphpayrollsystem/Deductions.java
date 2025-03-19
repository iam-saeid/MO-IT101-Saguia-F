
package com.mycompany.motorphpayrollsystem;


public class Deductions {
    
  public static float deductSSS(float basicSalary){
         
      if(basicSalary < 3250) {
          return 135.00f;
      } else if (basicSalary >= 3250 && basicSalary < 3750) {
          return 157.50f;
      } else if (basicSalary >= 3750 && basicSalary < 4250) {
          return 180.00f;
      } else if (basicSalary >= 4250 && basicSalary < 4750) {
          return 202.50f;
      } else if (basicSalary >= 4750 && basicSalary < 5250) {
          return 225.00f;
      } else if (basicSalary >= 5250 && basicSalary < 5750) {
          return 247.50f;
      } else if (basicSalary >= 5750 && basicSalary < 6250) {
          return 270.00f;
      } else if (basicSalary >= 6250 && basicSalary < 6750) {
          return 292.50f;
      } else if (basicSalary >= 6750 && basicSalary < 7250) {
          return 315.00f;
      } else if (basicSalary >= 7250 && basicSalary < 7750) {
          return 337.50f;
      } else if (basicSalary >= 7750 && basicSalary < 8250) {
          return 360.00f;
      } else if (basicSalary >= 8250 && basicSalary < 8750) {
          return 382.50f;
      } else if (basicSalary >= 8750 && basicSalary < 9250) {
          return 405.00f;
      } else if (basicSalary >= 9250 && basicSalary < 9750) {
          return 427.50f;
      } else if (basicSalary >= 9750 && basicSalary < 10250) {
          return 450.00f;
      } else if (basicSalary >= 10250 && basicSalary < 10750) {
          return 472.50f;
      } else if (basicSalary >= 10750 && basicSalary < 11250) {
          return 495.00f;
      } else if (basicSalary >= 11250 && basicSalary < 11750) {
          return 517.50f;
      } else if (basicSalary >= 11750 && basicSalary < 12250) {
          return 540.00f;
      } else if (basicSalary >= 12250 && basicSalary < 12750) {
          return 562.50f;
      } else if (basicSalary >= 12750 && basicSalary < 13250) {
          return 585.00f;
      } else if (basicSalary >= 13250 && basicSalary < 13750) {
          return 607.50f;
      } else if (basicSalary >= 13750 && basicSalary < 14250) {
          return 630.00f;
      } else if (basicSalary >= 14250 && basicSalary < 14750) {
          return 652.50f;
      } else if (basicSalary >= 14750 && basicSalary < 15250) {
          return 675.00f;
      } else if (basicSalary >= 15250 && basicSalary < 15750) {
          return 697.50f;
      } else if (basicSalary >= 15750 && basicSalary < 16250) {
          return 720.00f;
      } else if (basicSalary >= 16250 && basicSalary < 16750) {
          return 742.50f;
      } else if (basicSalary >= 16750 && basicSalary < 17250) {
          return 765.00f;
      } else if (basicSalary >= 17250 && basicSalary < 17750) {
          return 787.50f;
      } else if (basicSalary >= 17750 && basicSalary < 18250) {
          return 810.00f;
      } else if (basicSalary >= 18250 && basicSalary < 18750) {
          return 832.50f;
      } else if (basicSalary >= 18750 && basicSalary < 19250) {
          return 855.00f;
      } else if (basicSalary >= 19250 && basicSalary < 19750) {
          return 877.50f;
      } else if (basicSalary >= 19750 && basicSalary < 20250) {
          return 900.00f;
      } else if (basicSalary >= 20250 && basicSalary < 20750) {
          return 922.50f;
      } else if (basicSalary >= 20750 && basicSalary < 21250) {
          return 945.00f; 
      } else if (basicSalary >= 21250 && basicSalary < 21750) {
          return 967.50f;
      } else if (basicSalary >= 21750 && basicSalary < 22250) {
          return 990.00f;
      } else if (basicSalary >= 22250 && basicSalary < 22750) {
          return 1012.50f;
      } else if (basicSalary >= 22750 && basicSalary < 23250) {
          return 1035.00f;
      } else if (basicSalary >= 23250 && basicSalary < 23750) {
          return 1057.50f;
      } else if (basicSalary >= 23750 && basicSalary < 24750) {
          return 1080.00f;
      } else { 
      return 1125.00f;
      }
   }
  
   public static float deductPhilHealth(float basicSalary) {
       
       if (basicSalary <= 10000) {
           return 300.00f/2;
       } else if (basicSalary >= 10000.1f && basicSalary <= 59999.99f) {
           return (basicSalary * 0.03f)/2;
       } else {
           return 1800.00f / 2;
       }
    }
    
  public static float deductPagibig(float basicSalary) {
      return Math.min(basicSalary * 0.02f, 100);
  } 
  
  public static float withholdingTax( float taxableIncome) {
      if (taxableIncome <= 20832) {
            return 0;
        } else if (taxableIncome <= 33333) {
            return (taxableIncome - 20833) * 0.20f;
        } else if (taxableIncome <= 66667) {
            return 2500 + (taxableIncome - 33333) * 0.25f;
        } else if (taxableIncome <= 166667) {
            return 10833 + (taxableIncome - 66667) * 0.30f;
        } else if (taxableIncome <= 666667) {
            return 40833.33f + (taxableIncome - 166667) * 0.32f;
        } else {
            return 200833.33f + (taxableIncome - 666667) * 0.35f;
        }
  }
}
