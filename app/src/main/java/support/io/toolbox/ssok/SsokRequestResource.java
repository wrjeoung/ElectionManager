package support.io.toolbox.ssok;

/**
 * @desc 
 * 추후 쓰레드풀 관리를 위한 직접적인 네트워크 모듈을 만들예정 
 * @author
 * @date
 *
 */
public interface SsokRequestResource
{
	public String getName();

	public String getURL();

	public String getRequestContent();

	public void setResponseContent(String content);

	public void onPreExecute();

	public void onPostExecute();
}
