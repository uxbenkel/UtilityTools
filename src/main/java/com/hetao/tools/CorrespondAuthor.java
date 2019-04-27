package com.hetao.tools;

import java.util.ArrayList;

/**
 * @author hetao
 * @date 2019-04-27
 */
public class CorrespondAuthor {
    public static void main(String[] args) {
        String text = " W. R. Kerridge-Johns,* J. W. T. Geberbauer, and M. J. Damzen \n" +
                "Photonics Group, Department of Physics, The Blackett Laboratory, Imperial College London, London, SW7 2AZ, UK\n" +
                "*Corresponding author: wrk10@ic.ac.uk\n" +
                "\n" +
                "Author Affiliations\n" +
                " Surawut Wicharn1 and Prathan Buranasiri2,* \n" +
                "1Department of Physics, Faculty of Science, Srinakharinwirot University, Bangkok 10110, Thailand\n" +
                "2Department of Physics, Faculty of Science, King Mongkut’s Institute of Technology Ladkrabang, Bangkok 10520, Thailand\n" +
                "*Corresponding author: prathan.bu@kmitl.ac.th\n" +
                "\n" +
                "\n" +
                "Author Affiliations\n" +
                " Ruizhi Sun, Peiheng Zhou,* Wansen Ai, Yanning Liu, Ya Li, Ruomei Jiang, Wenxin Li, Xiaolong Weng, Lei Bi, and Longjiang Deng \n" +
                "National Engineering Research Center of Electromagnetic Radiation Control Materials, State Key Laboratory of Electronic Thin Film and Integrated Devices, University of Electronic Science and Technology of China, Chengdu 610054, China\n" +
                "*Corresponding author: phzhou@uestc.edu.cn\n" +
                "\n" +
                "Author Affiliations\n" +
                " Chao Sun,1 Xiang Lv,1 Beibei Ma,1 Jianbin Zhang,1 Dongmei Deng,1,* and Weiyi Hong1,2 \n" +
                "1Guangdong Provincial Key Laboratory of Nanophotonic Functional Materials and Devices, South China Normal University, Guangzhou 510631, \n" +
                "China\n" +
                "2hongwy@m.scnu.edu.cn\n" +
                "*Corresponding author: dmdeng@263.net\n" +
                "\n" +
                "Author Affiliations\n" +
                " Noelia Abascal Zorrilla,1,* Vincent Vantrepotte,1,2 Dat Dinh Ngoc,3 Nicolas Huybrechts,4,5 and Antoine Gardel1 \n" +
                "1Centre National de La Recherche Scientifique – CNRS, USR3456, 97334 Cayenne, French Guiana, France\n" +
                "2Laboratoire d'Océanologie et de Géosciences, Université du Littoral-Côte-d'Opale, CNRS, Université de Lille, 32 avenue Foch, Wimereux, France\n" +
                "3Space Technology Institute, Vietnam Academy of Science and Technology, 18 Hoang Quoc Viet, Cau Giay, Hanoi, Vietnam\n" +
                "4Cerema Eau Mer Fleuve, 134 rue de Beauvais, 60280 Margny Les Compiègne, France\n" +
                "5Sorbonne Université, Laboratoire Roberval –FRE UTC – CNRS 2012, Université de Technologie de Compiègne, Centre de Recherche de Royallieu CS 60319 – 60203 Compiègne, France\n" +
                "*Corresponding author: noelia.abascal-zorrilla@cnrs.fr\n" +
                "\n" +
                "\n" +
                "Author Affiliations\n" +
                " Malik Chami,1,2,* Xavier Lenot,3 Mireille Guillaume,4 Bruno Lafrance,3 Xavier Briottet,5 Audrey Minghelli,6 Sylvain Jay,4 Yannick Deville,7 and Véronique Serfaty8 \n" +
                "1Sorbonne Université, CNRS-INSU, Laboratoire Atmosphères Milieux Observations Spatiales (LATMOS), boulevard de l’Observatoire, CS 34229, 06304 Nice Cedex, France\n" +
                "2Institut Universitaire de France, Ministère de l'Education Nationale, de l'Enseignement Supérieur et de la Recherche, 1 rue Descartes, 75231 Paris Cedex 05, France\n" +
                "3CS Systèmes d’Information, 5 Rue Brindejonc des Moulinais, Parc de la Grande Plaine 31506 Toulouse, France\n" +
                "4Aix Marseille Université, CNRS, Centrale Marseille, Institut Fresnel, avenue Escadrille Normandie-Niémen, 13013 Marseille, France\n" +
                "5ONERA, DOTA, The French Aerospace Lab, 2 avenue Edouard Belin, 31055 Toulouse Cedex 4, France\n" +
                "6University of Toulon, CNRS, SeaTech, Laboratoire LSIS, UMR 7296, 83041 Toulon, France\n" +
                "7Institut de Recherche en Astrophysique et Planétologie (IRAP), Observatoire Midi-Pyrénées, Université de Toulouse, UPS-CNRS-OMP, 31400 Toulouse, France\n" +
                "8DGA/DS/MRIS, 60 boulevard du Général Martial Valin, CS 21623, 75509 Paris Cedex 15, France\n" +
                "*Corresponding author: malik.chami@upmc.fr\n" +
                "\n" +
                "Author Affiliations\n" +
                " Karlis Mikelsons1,2,* and Menghua Wang1 \n" +
                "1NOAA/NESDIS Center for Satellite Applications and Research, 5830 University Research Ct., College Park, Maryland 20740, USA\n" +
                "2Global Science and Technology, Inc., Greenbelt, Maryland 20770, USA\n" +
                "*Corresponding author: karlis.mikelsons@noaa.gov\n";
        String[] author_affiliations = text.split("Author Affiliations\n");
        System.out.println(author_affiliations.length);
        int count = 1;

        for (String author_affiliation : author_affiliations) {
            System.out.println("第" + count + "个：");
            String[] lines = author_affiliation.split("\\*Corresponding author: ");
            String authorsAndAddress = lines[0];
            int startIndex = authorsAndAddress.indexOf("\n");
            String authors = authorsAndAddress.substring(0, startIndex);
            //System.out.println(authors);
            String keyAuthor = " ";
            ArrayList<String> keyAddressNums = new ArrayList<>();
            String[] authorArray = authors.split("\\*");
            String correspondAuthors = authorArray[0];
            if (correspondAuthors.contains("and")) {
                String[] correspondAuthor = correspondAuthors.split("and ");
                String lastCorrespondAuthor = correspondAuthor[correspondAuthor.length - 1];
                if (lastCorrespondAuthor.split(",").length == 1) {
                    keyAuthor = lastCorrespondAuthor.replace(",", "");
                    if (keyAuthor.replace(" ", "").matches("\\S*\\d")) {
                        keyAddressNums.add(keyAuthor.substring(keyAuthor.length() - 1) + "");
                    }
                } else {
                    correspondAuthor = correspondAuthors.split(",");
                    int i = 1;
                    while (keyAuthor.matches(" |\\d")) {
                        keyAddressNums.add(keyAuthor);
                        keyAuthor = correspondAuthor[correspondAuthor.length - i];
                        i++;
                    }
                }
            } else {
                String[] correspondAuthor = correspondAuthors.split(",");
                int i = 1;
                while (keyAuthor.matches(" |\\d")) {
                    if (keyAuthor.matches("\\d")) {
                        keyAddressNums.add(keyAuthor);
                    }
                    keyAuthor = correspondAuthor[correspondAuthor.length - i];
                    if (keyAuthor.replace(" ", "").matches("\\S+\\d")) {
                        keyAddressNums.add(keyAuthor.substring(keyAuthor.length() - 1));
                    }
                    i++;
                }
            }
            System.out.println(keyAuthor.replaceAll("\\d", ""));
            String addresss = authorsAndAddress.substring(startIndex);
            //System.out.println(addresss);
            String keyAddress;
            if (!addresss.startsWith("\n1")) {
                keyAddress = addresss.replace("\n", "");
                System.out.println(keyAddress);
            } else {
                String[] keyAddresses = addresss.replace("\n1", "").split("\n\\d");
                assert false;
                for (String keyAddressNum : keyAddressNums) {
                    keyAddress = keyAddresses[Integer.parseInt(keyAddressNum) - 1].replace("\n", "");
                    System.out.println(keyAddress);
                }
            }
            String Mail = lines[1];
            System.out.println(Mail.replace("\n", ""));
            count++;
        }

    }
}
