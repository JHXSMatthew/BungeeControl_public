package com.github.JHXSMatthew.Controller;

public class MessageController {
	
	
	
	public String parseChinese(int i ){
		StringBuilder sb = new StringBuilder();
		
		if(i > 10){
			int tenth = i/10;
			switch(tenth){
			case 1:
				break;
			case 2:
				sb.append("贰");
				break;
			case 3:
				sb.append("叁");
				break;
			case 4:
				sb.append("肆");
				break;
			case 5:
				sb.append("伍");
				break;
			case 6:
				sb.append("陆");
				break;
			case 7 :
				sb.append("柒");
				break;
			case 8:
				sb.append("捌");
				break;
			case 9:
				sb.append("玖");
				break;
			}
			sb.append("拾");
		}
		
		i = i %10;
		switch(i){
		case 0:
			sb.append("拾");
			break;
		case 1:
			sb.append("壹");
			break;
		case 2:
			sb.append("贰");
			break;
		case 3:
			sb.append("叁");
			break;
		case 4:
			sb.append("肆");
			break;
		case 5:
			sb.append("伍");
			break;
		case 6:
			sb.append("陆");
			break;
		case 7 :
			sb.append("柒");
			break;
		case 8:
			sb.append("捌");
			break;
		case 9:
			sb.append("玖");
			break;
		}
		if(sb.toString() == null){
			return "未知";
		}
		return sb.toString();

	}
}
