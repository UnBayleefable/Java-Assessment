/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmonitoringapplication;

import java.util.ArrayList;

/**
 *
 * @author Shane Plater - 17.02.2017
 * this version is project specific and will not sort anything unless redesigned to
 */
public class SortingLibrary
{
    
    public static ArrayList BubbleSort(ArrayList<Object[]> arr)
    {
        for (int j = 0; j < arr.size(); j++)
        {
            for (int i = j + 1; i < arr.size(); i++)
            {
                if ((arr.get(i)[1].toString() + arr.get(i)[0].toString()).compareToIgnoreCase((arr.get(j)[1].toString() + arr.get(j)[0].toString())) < 0)
                {
                    Object[] temp = arr.get(j);
                    arr.set(j, arr.get(i));
                    arr.set(i, temp);
                }
            }
        }
        return arr;
    }

    public static ArrayList SelectionSort(ArrayList<Object[]> arr)
    {

        for (int i = arr.size() - 1; i >= 0; i--) // start at the end of the arry to be sorted and work backwards
        {
            int lowest = 0; // reset the lowest value for each pass
            for (int j = 0; j <= i; j++) //for each pass reduce the sweep by one because the final postion already contains the largest value
            {
                if ((arr.get(j)[2].toString() + arr.get(j)[0].toString()).compareToIgnoreCase((arr.get(lowest)[2].toString() + arr.get(lowest)[0].toString())) > 0)
               // if (arr.get(j)[0].toString().compareToIgnoreCase(arr.get(lowest)[0].toString()) > 0) // compare the current to the current lowest and if its smaller then set the current to the new lowest
                {
                    lowest = j;
                }
            }
            Object[] tmp = arr.get(i); // put current row in temp
            arr.set(i, arr.get(lowest)); // set the current row to the lowest row
            arr.set(lowest, tmp); // set the lowest original row to be the row stored in temp
        }
        return arr;
    }

    public static ArrayList InsertionSort(ArrayList<Object[]> arr)
    {
        for (int i = 1; i < arr.size(); i++)
        {
            Object[] temp = arr.get(i); // set temp object to object held in 2nd slot of array
            int j = i;
            //if ((arr.get(j)[2].toString() + arr.get(j)[0].toString()).compareToIgnoreCase((arr.get(lowest)[2].toString() + arr.get(lowest)[0].toString())) > 0)
            while (j > 0 && (arr.get(j - 1)[3].toString() + arr.get(j - 1)[0]).compareToIgnoreCase(temp[3].toString() + temp[0].toString()) > 0)
           // while (j > 0 && arr.get(j - 1)[0].toString().compareToIgnoreCase(temp[0].toString()) > 0) // compare j (current selected row in array) to array held in temp - if current row is larger than temp
            {   
                arr.set(j, arr.get(j - 1)); // place the previous row into current row
                j--; // used to move back through any previous iterations
            }
            arr.set(j, temp); // set the row stored in temp to the previous slot to the compared slot (because of the j-- the pointer is moved back one step)
        }
        return arr;
    }
}
