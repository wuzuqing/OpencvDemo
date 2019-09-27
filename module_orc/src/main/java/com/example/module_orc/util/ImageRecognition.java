// package com.example.module_orc.util;
//
// import org.opencv.calib3d.Calib3d;
// import org.opencv.core.*;
// import org.opencv.features2d.*;
// import org.opencv.imgcodecs.Imgcodecs;
// import org.opencv.imgproc.Imgproc;
//
// import java.util.ArrayList;
// import java.util.LinkedList;
// import java.util.List;
//
// /**
//  * Created by niwei on 2017/4/28.
//  */
// public class ImageRecognition {
//
//     private float nndrRatio = 0.7f;//这里设置既定值为0.7，该值可自行调整
//
//     private int matchesPointCount = 0;
//
//     public float getNndrRatio() {
//         return nndrRatio;
//     }
//
//     public void setNndrRatio(float nndrRatio) {
//         this.nndrRatio = nndrRatio;
//     }
//
//     public int getMatchesPointCount() {
//         return matchesPointCount;
//     }
//
//     public void setMatchesPointCount(int matchesPointCount) {
//         this.matchesPointCount = matchesPointCount;
//     }
//
//     public void matchImage(Mat templateImage, Mat originalImage) {
//         MatOfKeyPoint templateKeyPoints = new MatOfKeyPoint();
//         //指定特征点算法SURF
//         FastFeatureDetector featureDetector = FastFeatureDetector.create(FastFeatureDetector.SURF);
//         //获取模板图的特征点
//         featureDetector.detect(templateImage, templateKeyPoints);
//         //提取模板图的特征点
//         MatOfKeyPoint templateDescriptors = new MatOfKeyPoint();
//         DescriptorMatcher descriptorExtractor = DescriptorMatcher.create(DescriptorMatcher.SURF);
//         System.out.println("提取模板图的特征点");
//         descriptorExtractor.compute(templateImage, templateKeyPoints, templateDescriptors);
//
//         //显示模板图的特征点图片
//         Mat outputImage = new Mat(templateImage.rows(), templateImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
//         System.out.println("在图片上显示提取的特征点");
//         Features2d.drawKeypoints(templateImage, templateKeyPoints, outputImage, new Scalar(255, 0, 0), 0);
//
//         //获取原图的特征点
//         MatOfKeyPoint originalKeyPoints = new MatOfKeyPoint();
//         MatOfKeyPoint originalDescriptors = new MatOfKeyPoint();
//         featureDetector.detect(originalImage, originalKeyPoints);
//         System.out.println("提取原图的特征点");
//         descriptorExtractor.compute(originalImage, originalKeyPoints, originalDescriptors);
//
//         List<MatOfDMatch> matches = new LinkedList();
//         DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
//         System.out.println("寻找最佳匹配");
//         /**
//          * knnMatch方法的作用就是在给定特征描述集合中寻找最佳匹配
//          * 使用KNN-matching算法，令K=2，则每个match得到两个最接近的descriptor，然后计算最接近距离和次接近距离之间的比值，当比值大于既定值时，才作为最终match。
//          */
//         descriptorMatcher.knnMatch(templateDescriptors, originalDescriptors, matches, 2);
//
//         System.out.println("计算匹配结果");
//         LinkedList<DMatch> goodMatchesList = new LinkedList();
//
//         //对匹配结果进行筛选，依据distance进行筛选
//         for (MatOfDMatch match : matches) {
//             DMatch[] dmatcharray = match.toArray();
//             DMatch m1 = dmatcharray[0];
//             DMatch m2 = dmatcharray[1];
//
//             if (m1.distance <= m2.distance * nndrRatio) {
//                 goodMatchesList.addLast(m1);
//             }
//         }
//         matchesPointCount = goodMatchesList.size();
//         //当匹配后的特征点大于等于 4 个，则认为模板图在原图中，该值可以自行调整
//         if (matchesPointCount >= 4) {
//             System.out.println("模板图在原图匹配成功！");
//
//             List<KeyPoint> templateKeyPointList = templateKeyPoints.toList();
//             List<KeyPoint> originalKeyPointList = originalKeyPoints.toList();
//             LinkedList<Point> objectPoints = new LinkedList();
//             LinkedList<Point> scenePoints = new LinkedList();
//             for (DMatch goodMatch : goodMatchesList) {
//                 objectPoints.addLast(templateKeyPointList.get(goodMatch.queryIdx).pt);
//                 scenePoints.addLast(originalKeyPointList.get(goodMatch.trainIdx).pt);
//             }
//             MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
//             objMatOfPoint2f.fromList(objectPoints);
//             MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
//             scnMatOfPoint2f.fromList(scenePoints);
//             //使用 findHomography 寻找匹配上的关键点的变换
//             Mat homography = Calib3d.findHomography(objMatOfPoint2f, scnMatOfPoint2f, Calib3d.RANSAC, 3);
//
//             /**
//              * 透视变换(Perspective Transformation)是将图片投影到一个新的视平面(Viewing Plane)，也称作投影映射(Projective Mapping)。
//              */
//             Mat templateCorners = new Mat(4, 1, CvType.CV_32FC2);
//             Mat templateTransformResult = new Mat(4, 1, CvType.CV_32FC2);
//             templateCorners.put(0, 0, new double[] { 0, 0 });
//             templateCorners.put(1, 0, new double[] { templateImage.cols(), 0 });
//             templateCorners.put(2, 0, new double[] { templateImage.cols(), templateImage.rows() });
//             templateCorners.put(3, 0, new double[] { 0, templateImage.rows() });
//             //使用 perspectiveTransform 将模板图进行透视变以矫正图象得到标准图片
//             Core.perspectiveTransform(templateCorners, templateTransformResult, homography);
//
//             //矩形四个顶点
//             double[] pointA = templateTransformResult.get(0, 0);
//             double[] pointB = templateTransformResult.get(1, 0);
//             double[] pointC = templateTransformResult.get(2, 0);
//             double[] pointD = templateTransformResult.get(3, 0);
//
//             //指定取得数组子集的范围
//             int rowStart = (int) pointA[1];
//             int rowEnd = (int) pointC[1];
//             int colStart = (int) pointD[0];
//             int colEnd = (int) pointB[0];
//             Mat subMat = originalImage.submat(rowStart, rowEnd, colStart, colEnd);
//             Imgcodecs.imwrite("/Users/niwei/Desktop/opencv/原图中的匹配图.jpg", subMat);
//
//             //将匹配的图像用用四条线框出来
//             Rect rect = new Rect();
//             rect.x = (int) pointA[0];
//             rect.y = (int) pointA[1];
//             rect.width = (int) (pointD[0] - pointA[0]);
//             rect.height = (int) (pointD[1] - pointA[1]);
//             Imgproc.rectangle(originalImage, rect, new Scalar(0, 255, 0), 1, 8, 0);
//             MatOfDMatch goodMatches = new MatOfDMatch();
//             goodMatches.fromList(goodMatchesList);
//             Mat matchOutput = new Mat(originalImage.rows() * 2, originalImage.cols() * 2, Imgcodecs.IMREAD_COLOR);
//             Features2d.drawMatches(templateImage, templateKeyPoints, originalImage, originalKeyPoints, goodMatches, matchOutput, new Scalar(0, 255, 0), new Scalar(255, 0, 0),
//                 new MatOfByte(), 2);
//
//             Imgcodecs.imwrite("/Users/niwei/Desktop/opencv/特征点匹配过程.jpg", matchOutput);
//             Imgcodecs.imwrite("/Users/niwei/Desktop/opencv/模板图在原图中的位置.jpg", originalImage);
//         } else {
//             System.out.println("模板图不在原图中！");
//         }
//
//         Imgcodecs.imwrite("/Users/niwei/Desktop/opencv/模板特征点.jpg", outputImage);
//     }
//
//     public static void main(String[] args) {
//         System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//
//         String templateFilePath = "/Users/niwei/Desktop/opencv/模板.jpeg";
//         String originalFilePath = "/Users/niwei/Desktop/opencv/原图.jpeg";
//         //读取图片文件
//         Mat templateImage = Imgcodecs.imread(templateFilePath, Imgcodecs.IMREAD_GRAYSCALE);
//         Mat originalImage = Imgcodecs.imread(originalFilePath, Imgcodecs.IMREAD_GRAYSCALE);
//
//         ImageRecognition imageRecognition = new ImageRecognition();
//         imageRecognition.matchImage(templateImage, originalImage);
//
//         System.out.println("匹配的像素点总数：" + imageRecognition.getMatchesPointCount());
//     }
// }



// # 通过得到每个通道的直方图来计算相似度
//     def classify_hist_with_split(image1,image2,size = (256,256)):
//         # 将图像resize后，分离为三个通道，再计算每个通道的相似值
//         image1 = cv2.resize(image1,size)
//     image2 = cv2.resize(image2,size)
//     sub_image1 = cv2.split(image1)
//     sub_image2 = cv2.split(image2)
//     sub_data = 0
//         for im1,im2 in zip(sub_image1,sub_image2):
//     sub_data += calculate(im1,im2)
//     sub_data = sub_data/3
//         return sub_data

// # 计算单通道的直方图的相似值
//    def calculate(image1,image2):
//    hist1 = cv2.calcHist([image1],[0],None,[256],[0.0,255.0])
//    hist2 = cv2.calcHist([image2],[0],None,[256],[0.0,255.0])
//        # 计算直方图的重合度
//        degree = 0
// for i in range(len(hist1)):
//        if hist1[i] != hist2[i]:
//    degree = degree + (1 - abs(hist1[i]-hist2[i])/max(hist1[i],hist2[i]))
//        else:
//    degree = degree + 1
//    degree = degree/len(hist1)
// return degree
//
//     int aHash(Mat matSrc1, Mat matSrc2) {
//         Mat matDst1 = new Mat();
//         Mat matDst2 = new Mat();
//         Imgproc.resize(matSrc1, matDst1, new Size(8, 8), 0, 0, Imgproc.INTER_CUBIC);
//         Imgproc.resize(matSrc2, matSrc2, new Size(8, 8), 0, 0, Imgproc.INTER_CUBIC);
//         Imgproc.cvtColor(matDst1, matDst1, Imgproc.COLOR_BGRA2GRAY);
//         Imgproc.cvtColor(matSrc2, matSrc2, Imgproc.COLOR_BGRA2GRAY);
//         System.out.println(matSrc1 + " " + matSrc2);
//         int iAvg1 = 0, iAvg2 = 0;
//         int arr1[] = new int[64];
//         int arr2[] = new int[64];
//         for (int i = 0; i < 8; i++) { //row
//
//     int tmp = i * 8;
//
//     for (int j = 0; j < 8; j++) { //rol
//     int tmp1 = tmp + j;
//     double[] data1 = matDst1.get(i, j);
//     double[] data2 = matDst2.get(i, j);
//     System.out.println(String.format("%d,%d, value data1:%s data2:%s", i, j, data1 == null ? "" : Arrays.toString(data1),
//     data2 == null ? "" : Arrays.toString(data2)));
//     if (data1 == null || data2 == null) {
//     continue;
//     }
//     arr1[tmp1] = (int) (data1[0] / 4 * 4);
//     arr2[tmp1] = (int) (data2[0] / 4 * 4);
//
//     iAvg1 += arr1[tmp1];
//     iAvg2 += arr2[tmp1];
//     }
//     }
//
//     iAvg1 /= 64;
//     iAvg2 /= 64;
//
//     for (int i = 0; i < 64; i++) {
//     arr1[i] = (arr1[i] >= iAvg1) ? 1 : 0;
//     arr2[i] = (arr2[i] >= iAvg2) ? 1 : 0;
//     }
//
//     int iDiffNum = 0;
//
//     for (int i = 0; i < 64; i++) {
//     if (arr1[i] != arr2[i]) {
//     ++iDiffNum;
//     }
//     }
//
//     return iDiffNum;
//     }
//
//     int pHash(Mat matSrc1, Mat matSrc2) {
//
//     Mat hist_1 = new Mat();
//     Mat hist_2 = new Mat();
//     //颜色范围
//     MatOfFloat ranges = new MatOfFloat(0f, 256f);
//     //直方图大小， 越大匹配越精确 (越慢)
//     MatOfInt histSize = new MatOfInt(1000);
//
//     Imgproc.calcHist(Collections.singletonList(matSrc1), new MatOfInt(0), new Mat(), hist_1, histSize, ranges);
//     Imgproc.calcHist(Collections.singletonList(matSrc2), new MatOfInt(0), new Mat(), hist_2, histSize, ranges);
//
//     // CORREL 相关系数
//     double res = Imgproc.compareHist(hist_1, hist_2, Imgproc.CV_COMP_CORREL);
//
//     Log.d(TAG, "pHash: " + res);
//     // Mat matDst1 =new Mat();
//     // Mat matDst2 =new Mat();
//     // Imgproc.resize(matSrc1, matDst1,new Size(32, 32), 0, 0, Imgproc.INTER_CUBIC);
//     // Imgproc.resize(matSrc2, matDst2,new Size(32, 32), 0, 0, Imgproc.INTER_CUBIC);
//     // Imgproc.cvtColor(matDst1,matDst1,Imgproc.COLOR_BGRA2GRAY);
//     // Imgproc.cvtColor(matDst2,matDst2,Imgproc.COLOR_BGRA2GRAY);
//     //
//     // matDst1.convertTo(matDst1, CvType. CV_32F);
//     // matDst2.convertTo(matDst2,CvType. CV_32F);
//     //  dct(matDst1, matDst1);
//     // dct(matDst2, matDst2);
//     //
//     // int iAvg1 = 0, iAvg2 = 0;
//     // int arr1[] = new int[64];
//     // int arr2[] = new int[64];
//     //
//     // for (int i = 0; i < 8; i++)
//     // {
//     //     // int data1 = matDst1.
//     //     int tmp = i * 8;
//     //
//     //     for (int j = 0; j < 8; j++)
//     //     {
//     //         double[] data1 = matDst1.get(i, j);
//     //         double[] data2 = matDst2.get(i, j);
//     //         int tmp1 = tmp + j;
//     //         System.out.println(String.format("%d,%d, value data1:%s data2:%s", i, j,data1==null?"":Arrays.toString(data1),
//     //             data2==null?"":Arrays.toString(data2)));
//     //         if (data1==null || data2==null ){
//     //             continue;
//     //         }
//     //         arr1[tmp1] = (int) data1[0];
//     //         arr2[tmp1] = (int) data2[0];
//     //
//     //         iAvg1 += arr1[tmp1];
//     //         iAvg2 += arr2[tmp1];
//     //     }
//     // }
//     //
//     // iAvg1 /= 64;
//     // iAvg2 /= 64;
//     //
//     // for (int i = 0; i < 64; i++)
//     // {
//     //     arr1[i] = (arr1[i] >= iAvg1) ? 1 : 0;
//     //     arr2[i] = (arr2[i] >= iAvg2) ? 1 : 0;
//     // }
//     //
//     // int iDiffNum = 0;
//     //
//     // for (int i = 0; i < 64; i++)
//     //     if (arr1[i] != arr2[i])
//     //         ++iDiffNum;
//
//     return 0;
//     }