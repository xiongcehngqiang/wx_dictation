package indi.cyken.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import indi.cyken.constant.FEIPCodeEnum;
import indi.cyken.domain.Book;
import indi.cyken.domain.PageBean;
import indi.cyken.service.BookService;
import indi.cyken.utils.BeanFactory;
import indi.cyken.utils.JsonUtil;
import indi.cyken.utils.WriteBackUtil;
import net.sf.json.JSONObject;

/**
 * 课本相关servlet
 */
public class BookServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * 查询所有的课本
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BookService bs = (BookService) BeanFactory.getBean("BookService");
		List<Book> blist = null;
		try {
			blist = bs.findAll();
			for(int i=0;i<blist.size();i++) {
				Book tmpBook=blist.get(i);
				System.out.println(tmpBook.getBookid());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 2.将返回值转成json格式 返回到页面上
		request.setAttribute("blist", blist);
		String json = JsonUtil.list2json(blist);
		
		//3.写回去
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().println(json);
		
		return null;
	} 
	
	/**
	 * 通过id查询单个课本详情
	 * @throws Exception 
	 */
	public String  getById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.获取课本的id
		String bid=request.getParameter("bookid");
		
		//2.调用service
		//ProductService ps=new ProductServiceImpl();
		BookService ps=(BookService) BeanFactory.getBean("BookService");
		Book b=ps.getByBid(bid);
		if(b!=null && b.getBookid().length()>0) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().println(b.getBookname());
			return "success:查询成功";

		}
		
		return "fail:根据bookid查询课本失败";
		//return "/jsp/product_info.jsp";
	}
	
	
	/**
	 * 根据类别分页查询数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String  findByPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.获取类别 当前页  设置一个pagesize
		String cid=request.getParameter("cid");
		int currPage=Integer.parseInt(request.getParameter("currPage"));
		int pageSize=5;
		
		
		//2.调用service 返回值pagebean
		BookService ps=(BookService) BeanFactory.getBean("BookService");
		PageBean<Book> bean=ps.findByPage(currPage,pageSize,cid);
		
		//3.将结果放入request中 请求转发
		request.setAttribute("pb", bean);
		return "succes:按类别分页查询课本成功";
		//return "/jsp/product_list.jsp";
	}
	
	
	/**
	 * 通过分类ID获取该类下的所有课本
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String  WXGetBookByCategoryId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.获取商品的id
		String categoryid=request.getParameter("categoryid");
		System.out.println("接收到的课本分类ID："+categoryid);
		System.out.println("调用了BookServlet中的 WXGetBookByCategory 方法");
		BookService bs = (BookService) BeanFactory.getBean("BookService");
		List<Book> blist = null;
		try {
			blist = bs.getByCategoryId(categoryid);
			if(blist!=null&& blist.size()>0) {
				// 2.将返回值转成json格式 返回到页面上
				String json = JsonUtil.list2json(blist);
				//3.写回去
				JSONObject data=new JSONObject();
				data.put("categoryDetail", json);
				WriteBackUtil.WriteBackJsonStr(1, FEIPCodeEnum.OK.getCode(), FEIPCodeEnum.OK.getMsg(), data, response);
				return "success:根据categoryid获取课本成功";
			}else {
				return "success:根据categoryid获取课本失败";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "success:根据categoryid获取课本失败";
	}
	
	
	/**
	 * 微信端通过bookid获取课本信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String  WXGetBookByBookId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return getById(request,response);
	}
	
	
	/**
	 * 微信端通过获取课本信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String  WXGetAllBook(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("调用了BookServlet  WXGetAllBook方法");
		BookService bs = (BookService) BeanFactory.getBean("BookService");
		List<Book> blist = null;
		JSONObject data=new JSONObject();
		try {
			blist = bs.findAll();
			if(blist!=null) {
				String jsonstr=JsonUtil.list2json(blist);
				data.put("bookList", jsonstr);
				WriteBackUtil.WriteBackJsonStr(1, FEIPCodeEnum.OK.getCode(), FEIPCodeEnum.OK.getMsg(), data, response);
			}else {
				WriteBackUtil.WriteBackJsonStr(0, FEIPCodeEnum.FAIL.getCode(), FEIPCodeEnum.FAIL.getMsg(), data, response);

			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	

}
