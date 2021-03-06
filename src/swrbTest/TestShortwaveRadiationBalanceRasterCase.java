/*
 * This file is part of JGrasstools (http://www.jgrasstools.org)
 * (C) HydroloGIS - www.hydrologis.com 
 * 
 * JGrasstools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package swrbTest;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.jgrasstools.gears.io.rasterreader.OmsRasterReader;
import org.jgrasstools.gears.io.rasterwriter.OmsRasterWriter;
import org.jgrasstools.gears.io.shapefile.OmsShapefileFeatureReader;

import swrbPointCase.ShortwaveRadiationBalancePointCase;
import swrbRasterCase.ShortwaveRadiationBalanceRasterCase;

import org.jgrasstools.hortonmachine.utils.HMTestCase;

/**
 * Test the {@link Insolation} module.
 * 
 * @author Marialaura Bancheri
 */
public class TestShortwaveRadiationBalanceRasterCase extends HMTestCase {

	
	GridCoverage2D outDirectDataGrid = null;
	GridCoverage2D outDiffuseDataGrid = null;
	GridCoverage2D outTopATMDataGrid = null;

	public TestShortwaveRadiationBalanceRasterCase() throws Exception {

		String startDate = "2007-10-17 00:00" ;

		OmsRasterReader demReader = new OmsRasterReader();
		demReader.file = "resources/Input/DEM.asc";
		demReader.fileNovalue = -9999.0;
		demReader.geodataNovalue = Double.NaN;
		demReader.process();
		GridCoverage2D dem = demReader.outRaster;
		
		OmsRasterReader skyViewReader = new OmsRasterReader();
		skyViewReader.file = "resources/Input/sky.asc";
		skyViewReader.fileNovalue = -9999.0;
		skyViewReader.geodataNovalue = Double.NaN;
		skyViewReader.process();
		GridCoverage2D skyView = skyViewReader.outRaster;
		
		
		ShortwaveRadiationBalanceRasterCase SWRBRaster = new ShortwaveRadiationBalanceRasterCase();

		SWRBRaster.inDem = dem;
		SWRBRaster.inSkyview = skyView;
		SWRBRaster.inTempGrid=dem;
		SWRBRaster.inHumidityGrid=dem;
		SWRBRaster.tStartDate = startDate;
		SWRBRaster.doHourly=true;
		SWRBRaster.pCmO3=0.6;
		SWRBRaster.pAlphag=0.9;
		SWRBRaster.pVisibility=80;
		
		SWRBRaster.process();
		
		outDirectDataGrid  = SWRBRaster.outDirectGrid;
		outDiffuseDataGrid  = SWRBRaster.outDiffuseGrid;
		outTopATMDataGrid =SWRBRaster.outTopATMGrid;

		OmsRasterWriter writerDIrectraster = new OmsRasterWriter();
		writerDIrectraster .inRaster = outDirectDataGrid;
		writerDIrectraster .file = "resources/Output/mapDirect.asc";
		writerDIrectraster.process();

		OmsRasterWriter writerDiffuseraster = new OmsRasterWriter();
		writerDiffuseraster.inRaster = outDiffuseDataGrid;
		writerDiffuseraster.file = "resources/Output/mapDiffuse.asc";
		writerDiffuseraster.process();

		OmsRasterWriter writerTopATMraster = new OmsRasterWriter();
		writerTopATMraster.inRaster = outTopATMDataGrid;
		writerTopATMraster.file = "resources/Output/mapTop.asc";
		writerTopATMraster.process();


	}

}
