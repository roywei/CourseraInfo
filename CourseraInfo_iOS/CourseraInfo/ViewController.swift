//
//  ViewController.swift
//  CourseraInfo
//
//  Created by Lai Wei on 2/16/16.
//  Copyright © 2016 Lai Wei. All rights reserved.
//

import UIKit


class ViewController: UIViewController {
    @IBOutlet weak var SearchBar: UITextField!

   
    @IBOutlet weak var result1: UILabel!
    @IBOutlet weak var result2: UILabel!
    @IBOutlet weak var result3: UILabel!
    @IBOutlet weak var result4: UILabel!
    @IBOutlet weak var result5: UILabel!
    
//    override func viewDidLoad() {
//        super.viewDidLoad()
//        
//        //Looks for single or multiple taps.
//        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: "dismissKeyboard")
//        view.addGestureRecognizer(tap)
//    }
//    
//    //Calls this function when the tap is recognized.
//    func dismissKeyboard() {
//        //Causes the view (or one of its embedded text fields) to resign the first responder status.
//        view.endEditing(true)
//    }
    
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?){
        view.endEditing(true)
        super.touchesBegan(touches, withEvent: event)
    }


    
    @IBAction func doSearch() {
        //get search term
        
        let searchTerm = self.SearchBar.text!
        result1.text =  ""
        result2.text =  ""
        result3.text =  ""
        result4.text =  ""
        result5.text =  ""

        searchCourseraFor(searchTerm)
        print("search item: "+searchTerm)
        

    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        self.view.endEditing(true)
        return false
    }
  
    
    
    
    func searchCourseraFor(searchTerm: String) {
        
        let searchTerm = searchTerm.stringByReplacingOccurrencesOfString(" ", withString: "%20", options: NSStringCompareOptions.CaseInsensitiveSearch, range: nil)
        if let escapedSearchTerm = searchTerm.stringByAddingPercentEscapesUsingEncoding(NSUTF8StringEncoding) {
        let requestURL: NSURL = NSURL(string: "http://fast-bayou-9414.herokuapp.com/FetchCourse/\(escapedSearchTerm)")!
        let urlRequest: NSMutableURLRequest = NSMutableURLRequest(URL: requestURL)
        let session = NSURLSession.sharedSession()
        let task = session.dataTaskWithRequest(urlRequest) {
            (data, response, error) -> Void in
            
            let httpResponse = response as! NSHTTPURLResponse
            let statusCode = httpResponse.statusCode
            
            if (statusCode == 200) {
                print("Everyone is fine, file downloaded successfully.")
                do{
                    
                    let json = try NSJSONSerialization.JSONObjectWithData(data!, options:.AllowFragments)
                    for index in 1...5{
                        if let title = json["\(index)"] as? String{
                            print("result \(index):" + title)
                            dispatch_async(dispatch_get_main_queue()) {switch index {
                            case 1: self.result1.text = title
                            case 2: self.result2.text = title
                            case 3: self.result3.text = title
                            case 4: self.result4.text = title
                            case 5: self.result5.text = title
                            default: break
                                }}
                        }
                    }
                    
                }catch {
                    print("Error with Json: \(error)")
                }
                
            }
        }
        
    task.resume()
        }
        
    
    
    }
    


}

