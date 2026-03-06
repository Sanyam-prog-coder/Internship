import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//////////////////////////////////////////////////////////////////////////////////////////////////////////
/// 
/// Class       : RecommendationSystem
/// Description : Entry point function calls other function
/// Author      : Ravne Sanyam Bhypendrakumar
/// Date        : 06-March-2026
/// 
//////////////////////////////////////////////////////////////////////////////////////////////////////////

class RecommendationSystem
{
    static Map<String, Map<String, Integer>> userRating = new HashMap<>();

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : loadRatings
    /// Description : This function reads the movie ratings from a CSV file and stores them in a nested 
    ///                HashMap data structure
    /// Author      : Ravne Sanyam Bhupendrakuamr
    /// Date        : 06-March-2026
    /// 
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void loadRatings(String FileName)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(FileName)))
        {
            String line;
            br.readLine();

            while((line = br.readLine()) != null)
            {
                String Data[] = line.split(",");

                String User = Data[0];
                String Movie = Data[1];
                int Rating = Integer.parseInt(Data[2]);

                userRating.computeIfAbsent(User, k -> new HashMap<>()).put(Movie, Rating);
            }
        }
        catch(IOException e)
        {
            System.out.println("Error reading file : " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : recommendMovies
    /// Description : This function recommends movies to a user based on the ratings given by other 
    ///                 similar users. It calculates similarity between users and suggests movies that 
    //                  the target user has not rated
    /// Author      : Ravne Sanyam Bhupendrakuamr
    /// Date        : 06-March-2026
    /// 
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void recommendMovies(String User)
    {
        if(!userRating.containsKey(User))
        {
            System.out.println("USer Not Found");
            return;
        }

        Map<String , Integer> targetUser = userRating.get(User);

        Map<String, Double> scores = new HashMap<>();

        for(String otherUser : userRating.keySet())
        {
            if(otherUser.equals(User))
            {
                continue;
            }

            double similarity = calculateSimilarity(targetUser, userRating.get(otherUser));

            for(String Movie : userRating.get(otherUser).keySet())
            {
                if(!targetUser.containsKey(Movie))
                {
                    scores.put
                    (
                        Movie,
                        scores.getOrDefault(Movie, 0.0) +
                        similarity * userRating.get(otherUser).get(Movie)
                    );
                }
            }
        }

        System.out.println("\nRecommended Movies : ");

        scores.entrySet()
        .stream()
        .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
        .limit(3)
        .forEach(e -> System.out.println(e.getKey()));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : calculateSimilarity
    /// Description : function calculates the similarity between two users based on their movie ratings
    /// Author      : Ravne Sanyam Bhupendrakuamr
    /// Date        : 06-March-2026
    /// 
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public static double calculateSimilarity(Map<String, Integer> User1,
                                             Map<String, Integer> User2)
    {
        double dSum = 0;
        int iCount = 0;

        for(String Movie : User1.keySet())
        {
            if(User2.containsKey(Movie))
            {
                dSum = dSum + 1.0 / (1 + Math.abs(User1.get(Movie) - User2.get(Movie)));

                iCount++;
            }
        }

        return iCount == 0 ? 0 : dSum / iCount;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : main
    /// Description : Entry point function
    /// Author      : Ravne Sanyam Bhupendrakuamr
    /// Date        : 06-March-2026
    /// 
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String A[]) 
    {
        loadRatings("rating.csv");

        Scanner sobj = new Scanner(System.in);

        System.out.println("Enter User ID : ");
        String User = sobj.nextLine();

        recommendMovies(User);
    }
}