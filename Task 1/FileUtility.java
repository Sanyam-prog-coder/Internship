import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// 
/// Class       : FileManager
/// Description : This class contains all logic funtions
/// Author      : Ravne Sanyam Bhupendrakuamr
/// Date        : 04-March-2026
/// 
////////////////////////////////////////////////////////////////////////////////////////////////////////////

class FileManager
{
    static final String FILE_NAME = "sample.txt";

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : writeFile
    /// Description : Write File data
    /// Author      : Ravne Sanyam Bhupendrakuamr
    /// Date        : 04-March-2026
    /// 
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void writeFile(Scanner sobj)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true)))
        {
            System.out.println("Enter the text to wirte ");
            sobj.nextLine();
            String data = sobj.nextLine();

            writer.write(data);
            writer.newLine();

            System.out.println("Data written Succesfully...");
        }
        catch(IOException e)
        {
            System.out.println("Error writing file : "+e.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : readFile
    /// Description : Read File data
    /// Author      : Ravne Sanyam Bhupendrakuamr
    /// Date        : 04-March-2026
    /// 
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void readFile(Scanner sobj)
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME)))
        {
            String line;
            System.out.println("\n ------ File Content ------");
            while((line = reader.readLine()) != null)
                {
                    System.out.println(line);
                } 
        }
        catch(IOException e)
        {
            System.out.println("Error read file : "+e.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : modifyFile
    /// Description : Modify File data
    /// Author      : Ravne Sanyam Bhupendrakuamr
    /// Date        : 04-March-2026
    /// 
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void modifyFile(Scanner sobj)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME)))
        {
            System.out.println("Enter new Content of files : ");
            sobj.nextLine();
            String data = sobj.nextLine();

            writer.write(data);

            System.out.println("File Modified Succesfully...");
        }
        catch(IOException e)
        {
            System.out.println("Error Modify file : "+e.getMessage());
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// 
/// Class       : FileUtility
/// Description : This is Main class call all logic functions and hava Menu driven main
/// Author      : Ravne Sanyam Bhupendrakuamr
/// Date        : 04-March-2026
/// 
////////////////////////////////////////////////////////////////////////////////////////////////////////////

class FileUtility
{
    public static void main(String A[]) 
    {
        Scanner sobj = new Scanner(System.in);

        int Choice = 0;

        while(true)
        {
            System.out.println("\n--------- FILE HANDLING UTILITY ---------");
            System.out.println("1. Write File");
            System.out.println("2. Read File");
            System.out.println("3. Modify File");
            System.out.println("4. Exit");
            System.out.print("Enter your Choice : ");

            Choice = sobj.nextInt();

            switch(Choice)
            {
                case 1:
                    FileManager.writeFile(sobj);
                    break;
                case 2: 
                    FileManager.readFile(sobj);
                    break;
                case 3:
                    FileManager.modifyFile(sobj);
                    break;
                case 4:
                    System.out.println("Existing Program...");
                    System.exit(0);
                default :
                    System.out.println("Invalid choice. Try Again.");
            }
        }
    }
}
