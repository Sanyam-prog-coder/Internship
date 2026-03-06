import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//////////////////////////////////////////////////////////////////////////////////////////////////
/// 
/// Class       : WeatherService
/// Description : This Class Contens WeathreData And Service
/// Author      : Ravne Sanyam Bhupendrakumar
/// Date        : 04-March-2026
/// 
//////////////////////////////////////////////////////////////////////////////////////////////////

class WeatherService
{
    private String ApiKey;

    /////////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : WeatherService
    /// Description : It receives the API key from WeatherApp
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 04-March-2026
    /// 
    /////////////////////////////////////////////////////////////////////////////////////////////

    public WeatherService(String ApiKey)
    {
        this.ApiKey = ApiKey;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : getWeatherData
    /// Description : Build API URL using city and api key
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 04-March-2026
    /// 
    /////////////////////////////////////////////////////////////////////////////////////////////

    public String getWeatherData(String city) throws Exception
    {
        String apiURL = String.format(
                        "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric",
                        city.replace(" ", "%20"),
                        ApiKey
                            );
                            
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200)                   // Status code shows OK
        {
            return response.body();
        }
        else if (response.statusCode() == 401)              // Status code shows Unauthorized
        {
            throw new Exception("Invalid API Key (401)");
        }
        else if (response.statusCode() == 404)              // Status code shows Not Found
        {
            throw new Exception("City Not Found (404)");
        }
        else
        {
            throw new Exception("Error: " + response.statusCode());
        }
    }
}

//////////////////////////////////////////////////////////////////////////////////////////////////
/// 
/// Class       : WeatherParser
/// Description : This Class Contens parse And Display
/// Author      : Ravne Sanyam Bhupendrakumar
/// Date        : 04-March-2026
/// 
//////////////////////////////////////////////////////////////////////////////////////////////////

class WeatherParser
{

    /////////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : parseAndDisplay
    /// Description : Display the data recived from API 
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 04-March-2026
    /// 
    /////////////////////////////////////////////////////////////////////////////////////////////

    public void parseAndDisplay(String json)
    {
        String cityName = extractValue(json, "\"name\":\"", "\"");
        String temperature = extractValue(json, "\"temp\":", ",");
        String humidity = extractValue(json, "\"humidity\":", ",");
        String weather = extractValue(json, "\"description\":\"", "\"");

        System.out.println("\n===== WEATHER REPORT =====");
        System.out.println("City: " + cityName);
        System.out.println("Temperature: " + temperature + " °C");
        System.out.println("Humidity: " + humidity + " %");
        System.out.println("Condition: " + weather);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : extractValue
    /// Description : this function gives extracted value
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 04-March-2026
    /// 
    /////////////////////////////////////////////////////////////////////////////////////////////

    private String extractValue(String text, String start, String end)
    {
        int startIndex = text.indexOf(start);
        if(startIndex == -1) return "N/A";

        startIndex = startIndex + start.length();
        int endIndex = text.indexOf(end, startIndex);

        return text.substring(startIndex, endIndex);
    }
}

//////////////////////////////////////////////////////////////////////////////////////////////////
/// 
/// Class       : WeatherApp
/// Description : This is main class call all other class with logic
/// Author      : Ravne Sanyam Bhupendrakumar
/// Date        : 04-March-2026
/// 
//////////////////////////////////////////////////////////////////////////////////////////////////

class WeatherApp
{
    public static void main(String A[])
    {
        Scanner sobj = new Scanner(System.in);

        String City = null, Apikey = null;

        System.out.print("Enter City Name : ");
        City = sobj.nextLine().trim();

        System.out.print("Enter API key : ");
        Apikey = sobj.nextLine().trim();

        WeatherService service = new WeatherService(Apikey);

        try
        {
            String jsonResponse = service.getWeatherData(City);
            WeatherParser parser = new WeatherParser();
            parser.parseAndDisplay(jsonResponse);
        }
        catch(Exception e)
        {
            System.out.println("Error : "+e.getMessage());
        }
        sobj.close();
    }
}
//////////////////////////////////////////////////////////////////////////////////////////////////
/*

    Input   :
    Enter City Name : hingoli
    Enter API key : cc2b0ac064a08637e2e10faf43076838

    Output  :
    ===== WEATHER REPORT =====
    City: Hingoli
    Temperature: 35.21 °C
    Humidity: 13 %
    Condition: clear sky
 
    Input   :
    Enter City Name : pune
    Enter API key : cc2b0ac064a08637e2e10faf43076838

    Output  :
    ===== WEATHER REPORT =====
    City: Pune
    Temperature: 34.59 °C
    Humidity: 15 %
    Condition: clear sky

    Input   :
    Enter City Name : Purna
    Enter API key   : cc2b0ac064a08637e2e10faf430768388

    Output  :
    Error : Invalid API Key (401)

    Input   :
    Enter City Name : ABC
    Enter API key   : cc2b0ac064a08637e2e10faf43076838

    Output  :
    Error : City Not Found (404)

*/
//////////////////////////////////////////////////////////////////////////////////////////////////