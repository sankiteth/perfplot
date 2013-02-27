/** 
 * Georg Ofenbeck
 First created:
 * Date: 27/02/13
 * Time: 11:12 
 */

import HWCounters.Counter
import org.scalatest.Suite

import perfplot.Config
import perfplot.plot._
import perfplot.quantities._
import perfplot.services._

import java.io._
import scala.io._





class FFT_Overview extends Suite{
  val seq = Config.flag_c99 + Config.flag_hw + Config.flag_mkl_seq + Config.flag_optimization
  val parallel = Config.flag_c99 + Config.flag_hw + Config.flag_mkl + Config.flag_optimization

  val folder = new File (Config.result_folder + File.separator + "fft_overview" + File.separator)

  val counters = Array(
    Counter("10H","80H","FP_COMP_OPS_EXE.SSE_SCALAR_DOUBLE","Counts number of SSE* double precision FP scalar uops executed.",""),
    Counter("10H","10H","FP_COMP_OPS_EXE.SSE_FP_PACKED_DOUBLE","Counts number of SSE* double precision FP packed uops executed.",""),
    Counter("11H","02H","SIMD_FP_256.PACKED_DOUBLE","Counts 256-bit packed double-precision floating- point instructions.",""),
    Counter("B7H","01H","Custom_all","",     "0x3F80400FFF")
  )


  def test_FFT_MKL_seq() =
  {
    val sizes_2power =  (for (i<- 3 until 20) yield (Math.pow(2,i).toLong)).toList
    CodeGeneration.run_kernel(folder,CodeGeneration.fft_MKL,sizes_2power,"fft-MKL-warm",counters,true,true, seq)
    CodeGeneration.run_kernel(folder,CodeGeneration.fft_MKL,sizes_2power,"fft-MKL-cold",counters,true,false, seq)
    CodeGeneration.run_kernel(folder,CodeGeneration.fft_MKL,sizes_2power,"fft-MKL-parallel-warm",counters,true,true, parallel)
    CodeGeneration.run_kernel(folder,CodeGeneration.fft_MKL,sizes_2power,"fft-MKL-parallel-cold",counters,true,false, parallel)
  }


  def test_FFT_NR() =
  {
    val sizes_2power =  (for (i<- 3 until 20) yield (Math.pow(2,i).toLong)).toList
    CodeGeneration.run_kernel(folder,CodeGeneration.fft_NR,sizes_2power,"fft-NR-warm",counters,true,true, seq)
    CodeGeneration.run_kernel(folder,CodeGeneration.fft_NR,sizes_2power,"fft-NR-cold",counters,true,false, seq)
  }


  def test_FFT_Spiral() =
  {
    val sizes_2power =  (for (i<- 2 until 14) yield (Math.pow(2,i).toLong)).toList
    CodeGeneration.run_kernel(folder,CodeGeneration.fft_Spiral,sizes_2power,"fft-Spiral-warm",counters,false,true, seq)
    CodeGeneration.run_kernel(folder,CodeGeneration.fft_Spiral,sizes_2power,"fft-Spiral-cold",counters,false,false, seq)

    //GO: miss using the prec flag here - need to fix this!
    CodeGeneration.run_kernel(folder,CodeGeneration.fft_Spiral,sizes_2power,"fft-Spiral-vectorized-warm",counters,true,true, seq)
    CodeGeneration.run_kernel(folder,CodeGeneration.fft_Spiral,sizes_2power,"fft-Spiral-vectorized-cold",counters,true,false, seq)
  }

  def test_FFT_FFTW()=
  {
    val sizes_2power =  (for (i<- 3 until 20) yield (Math.pow(2,i).toLong)).toList
    CodeGeneration.run_kernel(folder,CodeGeneration.fft_FFTW,sizes_2power,"fft-FFTW-warm",counters,true,true, seq)
    CodeGeneration.run_kernel(folder,CodeGeneration.fft_FFTW,sizes_2power,"fft-FFTW-cold",counters,true,false, seq)
  }




}
